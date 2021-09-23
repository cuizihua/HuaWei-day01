package com.baizhi.flink.state

import org.apache.flink.api.common.functions.{AggregateFunction, RichMapFunction}
import org.apache.flink.api.common.state.{AggregatingState, AggregatingStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._

/**
 * 实时统计某App下用户的订单平均金额
 * 1.接收到的数据是来自于App的日志信息：用户下订单的日志信息
 *    用户编号 订单金额（整数，精确到元的单价）
 *
 * 2.通过AggregatingState完成的
 *
 */
object AggregatingStateJob {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //接收到的数据是 用户编号  订单金额
    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(Int, Int), Int] = dataStream
      .map(_.split("\\s+"))
      .map(array => (array(0).toInt, array(1).toInt))
      .keyBy(_._1)

    val result: DataStream[String] = keyedStream.map(new MyAggregatingStateMapFunction)

    result.print()


    environment.execute("aggregatingStateJob")
  }
}

class MyAggregatingStateMapFunction  extends RichMapFunction[(Int, Int), String]{

  //通过aggregatingState进行订单平均金额的计算
  //1.放进去的是订单的金额
  //2.从状态中获取到的，就是平均金额
  var aggregatingState:AggregatingState[Int,Double]=_



  override def open(parameters: Configuration): Unit = {

    //中间的类型(Int,Int):表示的是中间类型；计算过程中生成的数据的类型
    //这里写的(Int,Int),元组的第一个元素表示订单总金额，第二个表示订单个数
    val aggFunction: AggregateFunction[Int, (Int, Int), Double] = new AggregateFunction[Int, (Int, Int), Double] {
      //初始化方法：创建累加器：创建一个中间计算过程数据
      override def createAccumulator(): (Int, Int) = (0,0)

      //计算的，每使用一个aggregatingState.add方法，就会执行一次这个函数
      /**
       *
       * @param value 传递过来的数据
       * @param accumulator 上一次计算完成之后的数据
       * @return accumulator把这一次计算完成之后的数据返回
       */
      override def add(value: Int, accumulator: (Int, Int)): (Int, Int) = {
        val lastOrderTotalMoney: Int = accumulator._1
        val lastOrderCount: Int = accumulator._2
        (lastOrderTotalMoney+value,lastOrderCount+1)
      }

      /**
       * 获取结果；在使用aggregatingState.get方法的时候，会从这个方法的返回值获取到值
       * @param accumulator 中间的计算结果
       * @return 需要的数据
       */
      override def getResult(accumulator: (Int, Int)): Double = accumulator._1*1.0/accumulator._2

      /**
       * 合并的
       * @param a
       * @param b
       * @return
       */
      override def merge(a: (Int, Int), b: (Int, Int)): (Int, Int) = (a._1+b._1,a._2+b._2)
    }
    var aggregatingStateDescriptor:AggregatingStateDescriptor[Int,(Int,Int),Double]=new AggregatingStateDescriptor[Int,(Int,Int),Double]("asd",aggFunction,createTypeInformation[(Int,Int)])
    aggregatingState=getRuntimeContext.getAggregatingState(aggregatingStateDescriptor)
  }

  override def map(value: (Int, Int)): String = {

    //把接收到的订单金额放入到aggregatingState里面，由里面的运算函数完成自动计算处理
    aggregatingState.add(value._2)

    //构建返回值
    "编号是"+value._1+"的用户订单平均金额是："+aggregatingState.get()
  }
}