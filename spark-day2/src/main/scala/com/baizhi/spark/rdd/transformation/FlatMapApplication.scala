package com.baizhi.spark.rdd.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * flatMap的应用
 */
object FlatMapApplication {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("flatMapApp").setMaster("local[*]")

    val sc = new SparkContext(conf)

    val list = List("hello hi boy", "hello girl", "spark hello")

    val rdd: RDD[String] = sc.makeRDD(list)

    val rdd2: RDD[Array[String]] = rdd.map(_.split("\\s+"))
    val rdd3: RDD[String] = rdd.flatMap(_.split("\\s+"))


    sc.stop()
  }

}
