package com.baizhi.flink.window.trigger

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow

object TriggerJob {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    //windowAssigner:窗口分配器/分配员：怎么做窗口的划分
    //GlobalWindows.create()：全局窗口：无界数据流对应一个窗口
    //    val windowedStream: WindowedStream[(String, Int), String, TimeWindow] = keyedStream.window(GlobalWindows.create())

    val result: DataStream[(String, Int)] = keyedStream.window(GlobalWindows.create())
      .trigger(CountTrigger.of(3))//触发器的使用
      //CountTrigger:数量触发器
      .reduce((v1, v2) => (v1._1, v1._2 + v2._2))

    result.print()
    environment.execute("sessionWindowJob")
  }
}
