package com.baizhi.spark.rdd.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ReduceApplication {

  def main(args: Array[String]): Unit = {
    //spark rdd程序开发
    //1.创建sparkConf：在这里面配置master以及appName
    //2.通过sparkConf创建sparkContext==>sc

    //通过转换算子以及行动算子，完成业务需求

    //n.sc.stop()

    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("reduceApp")

    val sc = new SparkContext(sparkConf)

    val rdd: RDD[Int] = sc.makeRDD(1 to 10)

    /*val sum: Int = rdd.reduce((v1, v2) => v1 + v2)
    println(sum)*/
    //fold会给一个初始化值
    /*val sum: Int = rdd.fold(0)((v1, v2) => v1 + v2)
    println(sum)*/

    val sum: Double = rdd.aggregate(0.0)((v1, v2) => v1 + v2, (v1, v2) => v1 + v2)
    println(sum)


    sc.stop()
  }

}
