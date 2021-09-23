package com.baizhi.spark.rdd.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CountByKeyApplication {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("countByKeyApp").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[(String, Int)] = sc.makeRDD(List(("female", 1), ("male", 3), ("female", 8), ("male", 88),("male", 9)))

    val map: collection.Map[String, Long] = rdd.countByKey()
    println(map)


    sc.stop()
  }

}
