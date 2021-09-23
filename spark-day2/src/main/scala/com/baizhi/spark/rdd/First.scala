package com.baizhi.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object First {

  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("first")

    val sc = new SparkContext(sparkConf)

    //通过加载外部文件，创建rdd
    /*val rdd: RDD[String] = sc.textFile("d://a.txt")

    //rdd自带分区
    val numPartitions: Int = rdd.getNumPartitions*/

    val rdd: RDD[Int] = sc.parallelize(Array(12, 12, 3, 4, 4, 45, 6),2)
//    println(rdd.getNumPartitions)
    val rdd2: RDD[Int] = sc.makeRDD(List(1, 2),1)
    println(rdd2.getNumPartitions)


    1 to 10
    sc.stop()
  }

}
