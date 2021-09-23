package com.baizhi.flink.window.assigner

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

/**
 * 通过word count 看滚动窗口的理解
 */
object TumblingWindowJob {

  def main(args: Array[String]): Unit = {
    /**
     * 1.执行环境
     * 2.数据源：socket
     * 3.数据处理：flatmap/map/keyby==>keyedStream
     * 4.keyedStream.window==>划分窗口
     * 5.通过算子加载计算函数
     * 6.获取计算结果，展示
     * 7.执行
     */

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    //windowAssigner:窗口分配器/分配员：怎么做窗口的划分
    //TumblingProcessingTimeWindows.of(Time.seconds(5)):每5秒钟划分一个窗口，然后做计算
    val windowedStream: WindowedStream[(String, Int), String, TimeWindow] = keyedStream.window(TumblingProcessingTimeWindows.of(Time.seconds(5)))

    //在窗口上使用reduce算子完成窗口的计算
    val result: DataStream[(String, Int)] = windowedStream.reduce((v1, v2) => (v1._1, v1._2 + v2._2))
    result.print()
    environment.execute("tumblingWindowJob")
  }

}
