package com.baizhi.spark.rdd.cache

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object Test {

  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("cacheApp")

    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd: RDD[String] = sc.makeRDD(List("zs"))

    //把rdd中的元素做映射处理：在元素的后面拼接一个当前时间戳
    val rdd2: RDD[String] = rdd.map(e => e + "_" + System.currentTimeMillis())

    //一个rdd，要多次使用，每一次使用的时候都会重新计算
    /*println(rdd2.collect().mkString(","))//
    println(rdd2.collect().mkString(","))//
    */

    //如果一个rdd，要多次使用，为了不重新计算（提高执行效率）
    //可以把rdd缓存下来：存到内存中。再使用的时候，不会重新计算而是直接从内存中获取到直接使用
    //通过rdd提供的cache方法或者persist方法，完成rdd的缓存

    rdd2.cache()//默认的存储级别

    rdd2.persist()//默认的存储级别

    rdd2.persist(StorageLevel.DISK_ONLY_2)//可以重新指定存储级别

    println(rdd2.collect().mkString(","))//
    println(rdd2.collect().mkString(","))

    sc.stop()
  }

}
