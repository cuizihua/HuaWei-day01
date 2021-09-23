package com.baizhi.spark.rdd.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * map算子以及mapPartition算子
 */
object MapApplication {

  def main(args: Array[String]): Unit = {
    val array: Array[Int] = Array(1, 2, 3, 4, 5,6)

    //sparkConf/sparkContext
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("mapApp")

    val sc = new SparkContext(sparkConf)

    val rdd: RDD[Int] = sc.makeRDD(array,4)

    //把rdd中的每一个元素加5
    //map是一个转换算子，在这一行代码中并没有真正执行
//    val rdd2: RDD[Int] = rdd.map(_ + 5)

    /*val rdd2: RDD[Int] = rdd.map(e => {

      println("***********")
      e + 5
    })

    //collect:行动算子，获取到rdd中的元素
    val res: Array[Int] = rdd2.collect()
    println(res.mkString(","))//res.mkString(","):把数组中的每一个元素用,连接起来形成一个字符串
*/

    /*val rdd2: RDD[Int] = rdd.mapPartitions(iter => {
      println("**********")
      iter.map(_ + 5)
    })
    val res: Array[Int] = rdd2.collect()
    println(res.mkString(","))*/

    val rdd2: RDD[Int] = rdd.mapPartitionsWithIndex((index, iter) => {
      println("分区编号是"+index+"的分区中的元素为："+iter.mkString(" | "))
      iter
    })

    val res: Array[Int] = rdd2.collect()
    println(res.mkString(","))

    sc.stop()
  }

}
