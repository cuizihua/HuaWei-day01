package com.baizhi.spark.rdd.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SortByApplication {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sortByApp")

    val sc = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(Array(1, 12, 3, 4, 5, 2, 8, 6))

//    rdd.sortBy(e => e,true)//和下面的一行代码意义完全一样，都是按照元素值本身进行升序排列
    val rdd2: RDD[Int] = rdd.sortBy(e => e)

    val array: Array[Int] = rdd2.collect()

    //根据对3取余数进行排序,如果余数一样，再根据元素本身排序
    val rdd3: RDD[Int] = rdd.sortBy(e => (e % 3,e))

    val arr: Array[Int] = rdd3.collect()

    println(arr.mkString(","))


    sc.stop()
  }

}
