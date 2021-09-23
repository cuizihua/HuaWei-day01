package com.baizhi.spark.window

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object TestWindowApplication {

  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("windowApp")

    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(5))

    val dstream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop10", 9999)

    //windowDuration：就是窗口大小
    //slideDuration：就是滑动步长
    var windowDuration=Seconds(10)
    var slideDuration=Seconds(5)
    val dstream2: DStream[String] = dstream.window(windowDuration, slideDuration)

    dstream2.print()



    ssc.start()
    ssc.awaitTermination()
  }

}
