package com.baizhi.spark.rdd.transformation.kv

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object GroupByKeyApplication {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("groupByKeyApp").setMaster("local[*]")

    val sc = new SparkContext(conf)

    //groupByKey算子的使用，就需要有(key,value)结构的数据
    //通过单词的个数

    //通过spark rdd统计集合中每一个单词的个数
    val list = List("hello spark spark flink hive hive sqoop", "flume azkaban mr hdfs")

    /**
     * 分析
     * 1.把每一个单词都拉出来
     * 2.把每一个单词映射成一个元组(word,1)
     * 3.根据单词分组，然后在组内进行统计
     */
    val rdd: RDD[String] = sc.makeRDD(list)
    val rdd2: RDD[String] = rdd.flatMap(_.split("\\s+"))//1.把每一个单词都拉出来
    val rdd3: RDD[(String, Int)] = rdd2.map((_, 1))//2.把每一个单词映射成一个元组(word,1)
    //rdd3中的每一个元素就是(k,v)结构，就可以使用groupByKey

    //3.根据单词分组，然后在组内进行统计
    //把单词一样的划分到一个组里面
    val rdd4: RDD[(String, Iterable[Int])] = rdd3.groupByKey()
    //rdd4中元素是(单词,集合（1,1,1,1）)
    val rdd5: RDD[(String, Int)] = rdd4.map(e => (e._1, e._2.size))

    val arr: Array[(String, Int)] = rdd5.collect()
    println(arr.mkString(","))


    sc.stop()
  }

}
