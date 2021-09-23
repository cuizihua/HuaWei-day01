package com.baizhi.spark.rdd.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object GlomApplication {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("flatMapApp").setMaster("local[*]")

    val sc = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(1 to 10)

    val rdd3: RDD[Int] = rdd.mapPartitionsWithIndex((index, iter) => {
      println(index + ":" + iter.mkString(","))
      iter
    })
    rdd3.collect()

    //glom:把同一个分区中的数据形成数组
    val rdd2: RDD[Array[Int]] = rdd.glom()

    val rdd4: RDD[Array[Int]] = rdd2.mapPartitionsWithIndex((index, iter) => {
      //iter映射成一个字符串
      val strings: Iterator[String] = iter.map(array => array.mkString(";"))
      println(index + "=>" + strings.mkString(" | "))
      iter
    })

    rdd4.collect()

    rdd2.collect()//行动算子


    sc.stop()
  }

}
