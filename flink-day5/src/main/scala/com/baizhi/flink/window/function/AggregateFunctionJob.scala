package com.baizhi.flink.window.function

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

object AggregateFunctionJob {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    val windowedStream: WindowedStream[(String, Int), String, TimeWindow] = keyedStream.window(TumblingProcessingTimeWindows.of(Time.seconds(10)))


    val result: DataStream[String] = windowedStream.aggregate(new MyAggregateFunction)
    result.print()

    environment.execute("aggregateFunctionJob")
  }

}

class MyAggregateFunction extends AggregateFunction[(String,Int),(String,Int),String]{
  override def createAccumulator(): (String, Int) = ("",0)

  override def add(value: (String, Int), accumulator: (String, Int)): (String, Int) = {
    println(value)

    (value._1,value._2+accumulator._2)
  }

  override def getResult(accumulator: (String, Int)): String = accumulator._1+"的个数是"+accumulator._2

  override def merge(a: (String, Int), b: (String, Int)): (String, Int) = (a._1,a._2+b._2)
}
