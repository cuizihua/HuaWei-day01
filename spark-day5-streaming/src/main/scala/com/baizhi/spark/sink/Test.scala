package com.baizhi.spark.sink

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.ReceiverInputDStream

object Test {

  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sinkApp")

    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(5))

    val dstream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop10", 9999)


    //sink
    //1.print：应用在测试上面，直接把数据显示在控制台
    //2.把结果写入到磁盘文件：常用-生产环境

//    dstream.saveAsTextFiles("hdfs://hadoop10:9000/streaming/test")

   /* val date: Date = new Date()
    val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM")
    val str: String = format.format(date)
    dstream.saveAsTextFiles(s"hdfs://hadoop10:9000/streaming/${str}/test")*/

    //foreachRDD算子，可以把dstream中的rdd获取到，然后再写入到外部存储
    dstream.foreachRDD(rdd=>{
      rdd.foreach(str=>{
        //把数据写入到mysql、redis、hbase
        //这样写不是一个好的做法：每一个元素都会执行一次这个函数
      })

      rdd.foreachPartition(iter=>{
        //每一个分区执行一次这个函数
        //数据连接，就应该写在这里
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
