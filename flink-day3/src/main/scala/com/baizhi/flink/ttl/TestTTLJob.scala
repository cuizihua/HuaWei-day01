package com.baizhi.flink.ttl

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{StateTtlConfig, ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.common.time.Time

/**
 * 通过valueState理解ttl的使用
 * 通过valueState存储单词的个数，实现word count 功能
 */
object TestTTLJob {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)


    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    val result: DataStream[String] = keyedStream.map(new MyTTLMapFunction)
    result.print()

    environment.execute("testTTLJob")
  }

}
class MyTTLMapFunction extends RichMapFunction[(String, Int), String]{

  var valueState:ValueState[Int]=_



  override def open(parameters: Configuration): Unit = {

    val valueStateDescriptor: ValueStateDescriptor[Int] = new ValueStateDescriptor[Int]("vsd",createTypeInformation[Int])

    //通过descriptor的enableTimeToLive方法开启TTL
    var time:Time=Time.seconds(10)//设置state的ttl为10秒
    val stateTtlConfig: StateTtlConfig = StateTtlConfig
      .newBuilder(time)
      .build()
    valueStateDescriptor.enableTimeToLive(stateTtlConfig)

    valueState=getRuntimeContext.getState(valueStateDescriptor)
  }

  override def map(value: (String, Int)): String = {

    valueState.update(value._2+valueState.value())

    //构建返回值
    value._1+"单词的个数："+valueState.value()
  }
}
