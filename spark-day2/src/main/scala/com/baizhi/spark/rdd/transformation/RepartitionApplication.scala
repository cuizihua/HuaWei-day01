package com.baizhi.spark.rdd.transformation

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RepartitionApplication {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("repartitionApp")

    val sc = new SparkContext(conf)


    val rdd: RDD[Int] = sc.makeRDD(Array(1, 2, 3, 45, 5,42,43,44,41,46,47,74,774,84,884), 2)

    val rdd3: RDD[Int] = rdd.mapPartitionsWithIndex((index, iter) => {
      println(index+"***"+iter.mkString(","))
      iter
    })

    rdd3.collect()

    val rdd2: RDD[Int] = rdd.coalesce(3,true)//不会产生shuffle

    val rdd4: RDD[Int] = rdd2.mapPartitionsWithIndex((index, iter) => {
      println(index+"^^"+iter.mkString(","))
      iter
    })

    rdd4.collect()

//    rdd.repartition(2)//coalesce(numPartitions, shuffle = true);所以repartition的本质就是coalesce

    rdd2.collect()


    sc.stop

  }

}
