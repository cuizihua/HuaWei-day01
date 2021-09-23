package com.baizhi.spark.rdd.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object GroupByApp {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("flatMapApp").setMaster("local[*]")

    val sc = new SparkContext(conf)

    val rdd: RDD[Int] = sc.makeRDD(1 to 10)

    //对rdd中元素进行分组：按除以3的余数进行分组
    //0:3,6,9;1:1,4,7,10;2:2,5,8,
    val rdd2: RDD[(Int, Iterable[Int])] = rdd.groupBy(_ % 3)

    val array: Array[(Int, Iterable[Int])] = rdd2.collect()

    array.foreach(t=>{
      println(t._1+":"+t._2.mkString(","))
    })


    sc.stop()
  }

}
