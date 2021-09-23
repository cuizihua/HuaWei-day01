package com.baizhi.spark.rdd.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object DistinctApplication {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("filterApp")

    val sc = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(Array(1, 2, 3, 4, 4, 4, 5, 4, 2, 3))

    val rdd2: RDD[Int] = rdd.distinct()

    val array: Array[Int] = rdd2.collect()

    println(array.mkString(","))


    sc.stop()
  }
}
