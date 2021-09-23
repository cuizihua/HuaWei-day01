package com.baizhi.spark.transform

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object TestApplication {

  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("transformApp")

    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(5))

    val dstream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop10", 9999)

//    val dstream3: DStream[Int] = dstream.map(str => str.toInt)


//    val rdd2: RDD[Int] = ssc.sparkContext.makeRDD(List(1, 2, 3, 4))


    //1.transform算子就是一个转换算子，执行完成之后，返回还是DStream
    //2.transform算子里面的函数是对rdd的操作
    //也就是说，在流计算中，可以把流中的rdd做对应的操作，就是使用rdd算子完成操作处理
    //函数执行完成之后，需要返回rdd对象
//    val dstream2: DStream[Int] = dstream.transform((rdd:RDD[String]) => {
//      rdd.map(str=>str.toInt)
//    })


    val dstream2: DStream[(String, Int)] = dstream.map((_, 1))
    val rdd3: RDD[(String, Int)] = ssc.sparkContext.makeRDD(List(("a", 5), ("b", 8)))

    //在transform里面可以对rdd进行操作。使用rdd算子完成一些处理
    val dstream4: DStream[(String, (Int, Int))] = dstream2.transform((rdd: RDD[(String, Int)]) => {
      rdd.join(rdd3)
    })

    ssc.start()
    ssc.awaitTermination()
  }

}
