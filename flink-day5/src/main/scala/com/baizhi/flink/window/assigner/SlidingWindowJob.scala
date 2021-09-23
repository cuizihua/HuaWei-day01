package com.baizhi.flink.window.assigner

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.{SlidingProcessingTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

object SlidingWindowJob {
  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    //windowAssigner:窗口分配器/分配员：怎么做窗口的划分
    //SlidingProcessingTimeWindows.of(Time.seconds(10),Time.seconds(5)):窗口大小是10秒；滑动步长是5秒
    val windowedStream: WindowedStream[(String, Int), String, TimeWindow] = keyedStream.window(SlidingProcessingTimeWindows.of(Time.seconds(10),Time.seconds(5)))

    //在窗口上使用reduce算子完成窗口的计算
    val result: DataStream[(String, Int)] = windowedStream.reduce((v1, v2) => (v1._1, v1._2 + v2._2))
    result.print()
    environment.execute("tumblingWindowJob")
  }

}
