package com.baizhi.spark.rdd.transformation.kv

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 统计男女人数
 */
object AggregateByKeyApplication {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("AggregateByKeyApplication").setMaster("local[*]")

    val sc = new SparkContext(conf)

    //list中元素的含义是(性别，人数)
    val list = List(("female", 3), ("male", 5), ("female", 5), ("male", 6),("female", 4), ("male", 9),("female", 7), ("male", 10))

    val rdd: RDD[(String, Int)] = sc.makeRDD(list,2)

    //看一下第一个分区和第二个分区分别有哪些数据
    //mapPartitionWithIndex
    /*val rdd2: RDD[(String, Int)] = rdd.mapPartitionsWithIndex((index, iter) => {
      println(index+":"+iter.mkString(","))
      iter
    })

    rdd2.collect()*/

    val rdd2: RDD[(String, Int)] = rdd.aggregateByKey(0)((u, v) => {
      println(u+"^^"+v)
      u + v
    }, (u1, u2) => {
      println(u1+"%%"+u2)
      u1 + u2
    })

    val rdd3: RDD[(String, Int)] = rdd2.mapPartitionsWithIndex((index, iter) => {
      println(index + ":" + iter.mkString(","))
      iter
    })

    rdd3.collect()

    val arr: Array[(String, Int)] = rdd2.collect()
    println(arr.mkString(","))

    sc.stop()

  }

}
