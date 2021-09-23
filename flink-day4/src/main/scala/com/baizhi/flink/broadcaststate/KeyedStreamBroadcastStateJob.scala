package com.baizhi.flink.broadcaststate

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.common.state.{BroadcastState, MapStateDescriptor, ReadOnlyBroadcastState, ReducingState, ReducingStateDescriptor}
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

/**
 * 根据分类的订单总金额会有对应的优惠信息：dz类别下，总订单金额超过了3000，发放3000块优惠券
 * 广播状态的使用，基于keyedStream的广播状态的使用
 * 订单流以及优惠信息流
 * 订单流：每一个用户的订单
 * 优惠信息流：什么类别下，总金额达到多少，有多少优惠券
 */
object KeyedStreamBroadcastStateJob {

  def main(args: Array[String]): Unit = {

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //数据中会包含 用户编号，商品类别，订单金额
    val orderStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)
    /*val highStream: KeyedStream[(Int, OrderItem), Int] = orderStream
      .map(_.split("\\s+"))
      .map(array => (array(0).toInt+":"+array(1), new OrderItem(array(0).toInt, array(1), array(2).toDouble)))//word count
      .keyBy(_._1)*/
    val dataStream2: DataStream[OrderItem] = orderStream
      .map(_.split("\\s+"))
      .map(array => new OrderItem(array(0).toInt, array(1), array(2).toDouble))
    val keyedStream: KeyedStream[OrderItem, Tuple] = dataStream2.keyBy("userId","categoryName")

    //数据中会包含  商品类别，金额阈值，优惠券金额
    val youHuiStream: DataStream[String] = environment.socketTextStream("hadoop10", 8888)
    val lowStream: DataStream[YouHuiInfo] = youHuiStream
      .map(_.split("\\s+"))
      .map(array => new YouHuiInfo(array(0), array(1).toDouble, array(2).toDouble))

    val mapStateDescriptor: MapStateDescriptor[String, YouHuiInfo] = new MapStateDescriptor[String, YouHuiInfo]("msd", createTypeInformation[String], createTypeInformation[YouHuiInfo])
    val broadcastStream: BroadcastStream[YouHuiInfo] = lowStream.broadcast(mapStateDescriptor)

    val bcs: BroadcastConnectedStream[OrderItem, YouHuiInfo] = keyedStream.connect(broadcastStream)

    val result: DataStream[String] = bcs.process(new MyKeyedBroadcastProcessFunction(mapStateDescriptor))

    result.print()



    environment.execute("keyedStreamBroadcastStateJob")

  }

}


class MyKeyedBroadcastProcessFunction(mapStateDescriptor: MapStateDescriptor[String, YouHuiInfo]) extends KeyedBroadcastProcessFunction[Tuple,OrderItem, YouHuiInfo,String]{



  var reducingState:ReducingState[Double]=_


  override def open(parameters: Configuration): Unit = {

    reducingState=getRuntimeContext.getReducingState(new ReducingStateDescriptor[Double]("rsd",new ReduceFunction[Double] {
      override def reduce(value1: Double, value2: Double): Double = value1+value2
    },createTypeInformation[Double]))
  }

  override def processElement(value: OrderItem, ctx: KeyedBroadcastProcessFunction[Tuple, OrderItem, YouHuiInfo, String]#ReadOnlyContext, out: Collector[String]): Unit = {

    val state: ReadOnlyBroadcastState[String, YouHuiInfo] = ctx.getBroadcastState(mapStateDescriptor)

    if(state.contains(value.categoryName)){
      //已经设置了优惠券
      val info: YouHuiInfo = state.get(value.categoryName)

      reducingState.add(value.money)

      //获取到订单总金额
      val totalMoney: Double = reducingState.get()
      println(totalMoney)
      if(totalMoney>=info.threshold){
        out.collect("在"+value.categoryName+"这个分类下，发放了优惠券"+info.youHuiQuan)
        reducingState.clear()//优惠券发放完成之后，就应该把订单总金额清零
      }else{
        out.collect("在"+value.categoryName+"这个分类下，再消费"+(info.threshold-totalMoney)+"可以会到"+info.youHuiQuan)
      }
    }else{
      //还没有设置优惠券
      out.collect("还没有优惠信息")
      reducingState.add(value.money)
    }
  }

  override def processBroadcastElement(value: YouHuiInfo, ctx: KeyedBroadcastProcessFunction[Tuple, OrderItem, YouHuiInfo, String]#Context, out: Collector[String]): Unit = {

    val state: BroadcastState[String, YouHuiInfo] = ctx.getBroadcastState(mapStateDescriptor)

    state.put(value.categoryName,value)

  }
}

/**
 * 订单详情类
 * @param userId 用户编号
 * @param categoryName 类别
 * @param money 订单详情总金额
 */
case class OrderItem(userId:Int,categoryName:String,money:Double)

/**
 * 优惠券信息类
 * @param categoryName 商品类别
 * @param threshold 订单总金额达到的阈值
 * @param youHuiQuan 优惠券金额
 */
case class YouHuiInfo(categoryName:String,threshold:Double,youHuiQuan:Double)
