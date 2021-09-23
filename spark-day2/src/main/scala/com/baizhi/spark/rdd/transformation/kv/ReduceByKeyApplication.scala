package com.baizhi.spark.rdd.transformation.kv

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * reduceByKey
 * 根据key对value进行聚合
 */
object ReduceByKeyApplication {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("reduceByKeyApp").setMaster("local[*]")

    val sc = new SparkContext(conf)

    //list中元素的含义是(性别，人数)
    val list = List(("female", 3), ("male", 5), ("female", 5), ("male", 6))

    //通过rdd计算每一个性别的人数总和
    val rdd: RDD[(String, Int)] = sc.makeRDD(list)
    //rdd中的元素就是(key,value)结构

    //通过groupByKey实现
    /*val rdd2: RDD[(String, Iterable[Int])] = rdd.groupByKey()
    val rdd3: RDD[(String, Int)] = rdd2.map(e => (e._1, e._2.sum))
    val arr: Array[(String, Int)] = rdd3.collect()
    println(arr.mkString(","))*/

    //就下面这个代码来说，会对每一个key的所有value做加法运算
    val rdd2: RDD[(String, Int)] = rdd.reduceByKey((e1, e2) => e1 + e2)
    val arr: Array[(String, Int)] = rdd2.collect()
    println(arr.mkString(","))




    sc.stop()
  }

}
