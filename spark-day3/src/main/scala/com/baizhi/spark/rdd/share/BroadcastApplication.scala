package com.baizhi.spark.rdd.share

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object BroadcastApplication {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("BroadcastApp").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[String] = sc.makeRDD(List("hello", "spark", "flink", "hive", "sqoop", "mr", "hdfs"))

    //把包含某一个字符的单词排除掉
    val set: Set[String] = Set("a", "i", "r", "s")//数据量特别大

   /* val rdd2: RDD[String] = rdd.filter(e => {
      //      !e.contains("a")//e中如果包含有a，就不要这个e

      //这一行代码，是把set集合复制到了所有的task里面
      //每一个task里面都有一份set集合
      val setBoolean: Set[Boolean] = set.map(v => !e.contains(v))
      setBoolean.reduce((v1, v2) => v1 && v2)
    })*/

    //如果set集合的数据量比较大，在每一个task里面备份一份，会导致executor总的使用内存比较大
    //可以通过广播变量的方式，把set集合备份到executor，而不是每一个task里面存储一份

    val broadcast: Broadcast[Set[String]] = sc.broadcast(set)
    val rdd2: RDD[String] = rdd.filter(e => {
      //      !e.contains("a")//e中如果包含有a，就不要这个e

      //broadcast.value是从广播变量中获取到的数据，就是从executor中拿到的数据，在task里面执行计算
      //broadcast.value是不会在task中备份数据的
      val setBoolean: Set[Boolean] = broadcast.value.map(v => !e.contains(v))
      setBoolean.reduce((v1, v2) => v1 && v2)
    })


    rdd2.foreach(println(_))


    sc.stop()
  }

}
