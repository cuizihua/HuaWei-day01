package com.baizhi.spark.rdd.checkpoint

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object Test {

  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("checkpointApp")

    val sc: SparkContext = new SparkContext(sparkConf)

    sc.setCheckpointDir("d:/checkpoint-dir")

    val rdd: RDD[String] = sc.makeRDD(List("zs"))

    //把rdd中的元素做映射处理：在元素的后面拼接一个当前时间戳
    val rdd2: RDD[String] = rdd.map(e => e + "_" + System.currentTimeMillis())


    //对rdd2进行持久化
    rdd2.checkpoint()

    //rdd一旦持久化，再使用的时候就会从持久化目录中加载数据，而不会重新计算

    println(rdd2.collect().mkString(","))//
    println(rdd2.collect().mkString(","))
    println(rdd2.collect().mkString(","))

    sc.stop()
  }

}
