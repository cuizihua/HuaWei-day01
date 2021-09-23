package com.baizhi.spark.rdd.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object ForeachApplication {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("foreachApp").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)


    val rdd: RDD[Int] = sc.makeRDD(1 to 10)

    /*rdd.foreach(e=>{
      //代码
      println(e)

      //把rdd中的数据写入到mysql
      //自己写jdbc代码
    })*/

    rdd.foreachPartition(iter=>{
      //
      //创建一个jedis：连接
      iter.foreach(e=>{
        //把e这个数据添加到redis中
      })
    })


    sc.stop()

  }

}
