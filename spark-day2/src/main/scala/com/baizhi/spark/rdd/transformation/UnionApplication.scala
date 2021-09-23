package com.baizhi.spark.rdd.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object UnionApplication {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("UnionApp")

    val sc = new SparkContext(conf)

    val rdd1: RDD[Int] = sc.makeRDD(Array(2, 3, 5, 6, 7))
    val rdd2: RDD[Int] = sc.makeRDD(Array(9, 8, 5, 6, 7))

    /*val unionRDD: RDD[Int] = rdd1.union(rdd2)
    val arr: Array[Int] = unionRDD.collect()
    println(arr.mkString(","))*/

    /*val subRDD: RDD[Int] = rdd2.subtract(rdd1)
    val arr: Array[Int] = subRDD.collect()
    println(arr.mkString(","))*/

    val interRDD: RDD[Int] = rdd1.intersection(rdd2)
    val arr: Array[Int] = interRDD.collect()
    println(arr.mkString(","))


    sc.stop()
  }

}
