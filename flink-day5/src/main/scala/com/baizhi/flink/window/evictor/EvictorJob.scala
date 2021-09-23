package com.baizhi.flink.window.evictor

import java.{lang, util}
import java.util.Iterator

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.evictors.Evictor
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.runtime.operators.windowing.TimestampedValue

import scala.collection.JavaConverters._

object EvictorJob {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val dataStream2: DataStream[String] = dataStream
      .flatMap(_.split("\\s+"))



    val allWindowedStream: AllWindowedStream[String, TimeWindow] = dataStream2.windowAll(TumblingProcessingTimeWindows.of(Time.seconds(5)))

    val result: DataStream[String] = allWindowedStream
      .evictor(new MyEvictor)//使用自定义的剔除器：把包含a的元素，计算之前剔除掉
      .reduce((v1, v2) => {
        println("^^^^^^^")
        v1 + "|" + v2
      })

    result.print()


    environment.execute("tumblingWindowJob")
  }

}

class MyEvictor extends Evictor[String, TimeWindow]{
  //计算函数执行之前做剔除操作
  /**
   * 把包含a的单词剔除掉
   * @param elements 窗口中的所有元素
   * @param size 窗口中元素的个数
   * @param window 窗口
   * @param evictorContext 剔除器上下文对象
   */
  override def evictBefore(elements: lang.Iterable[TimestampedValue[String]], size: Int, window: TimeWindow, evictorContext: Evictor.EvictorContext): Unit = {

//    println("************")

    //


    /*val iterable: Iterable[TimestampedValue[String]] = elements.asScala
    val iterable1: Iterable[TimestampedValue[String]] = iterable.filter(tsv => (!tsv.getValue.contains("a")))
    elements=iterable1.asJava*/
    val iterator: util.Iterator[TimestampedValue[String]] = elements.iterator
    while (iterator.hasNext) {
      val tsv: TimestampedValue[String] = iterator.next
      if(tsv.getValue.contains("a")) {
        iterator.remove()//从迭代器中移除掉
      }
    }


  }

  //计算函数执行之后做剔除操作
  override def evictAfter(elements: lang.Iterable[TimestampedValue[String]], size: Int, window: TimeWindow, evictorContext: Evictor.EvictorContext): Unit = {

//    println("&&&&&&&&&&&")

  }
}
