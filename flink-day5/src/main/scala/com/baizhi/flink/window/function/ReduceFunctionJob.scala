package com.baizhi.flink.window.function

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time

object ReduceFunctionJob {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    val result: DataStream[(String, Int)] = keyedStream
      .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
      .reduce(new MyReduceFunction)//reduceFunction的使用

    result.print()


    environment.execute("reduceFunctionJob")
  }

}

/**
 * 每过来一个元素，都会执行一次这个方法
 */
class MyReduceFunction extends ReduceFunction[(String, Int)]{
  override def reduce(value1: (String, Int), value2: (String, Int)): (String, Int) = {
    println(value2)
    (value1._1,value1._2+value2._2)
  }
}
