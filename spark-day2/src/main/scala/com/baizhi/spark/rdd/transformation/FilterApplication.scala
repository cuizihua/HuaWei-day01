package com.baizhi.spark.rdd.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object FilterApplication {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("filterApp")

    val sc = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("a.jpg", "c.jpeg", "d.gif", "a.txt", "e.png", "a.pdf"))
    //把普通留下来：.jpg/.gif/.png/.jpeg
    val rdd2: RDD[String] = rdd.filter(fileName => fileName.endsWith(".jpg") | fileName.endsWith(".png") | fileName.endsWith(".gif") | fileName.endsWith(".jpeg"))

    val array: Array[String] = rdd2.collect()
    println(array.mkString(","))



    sc.stop()
  }

}
