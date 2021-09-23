package com.baizhi.spark.rdd.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 行动算子：
 * count():获取到rdd中元素的个数
 * first():获取到rdd中的第一个元素
 * take(n):获取到rdd中的前n个元素
 * takeOrdered(n):获取到排序之后的前n个元素
 */
object CountApplication {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("countApp").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(Array(11, 3, 4, 8, 19, 2, 7, 5))

    //count
//    println(rdd.count())

    //first
//    println(rdd.first())
    
    
    //take(n)
    /*val array: Array[Int] = rdd.take(5)
    println(array.mkString(","))*/

    //takeOrdered(n)
   /* val array: Array[Int] = rdd.takeOrdered(5)
    println(array.mkString(","))*/

    //取RDD中的最大的5个数
    //takeOrdered(5):默认是获取的升序排列的前5个
    //这个函数还可以跟一个参数，是Ordering。就是排序规则
    val array: Array[Int] = rdd.takeOrdered(5)(Ordering.Int.reverse)
    println(array.mkString(","))

    sc.stop()
  }

}
