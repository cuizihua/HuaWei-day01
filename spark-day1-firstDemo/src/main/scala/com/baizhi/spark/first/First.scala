package com.baizhi.spark.first

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
 * 需求：
 * 加载hdfs根目录下的a.txt文件，统计单词的个数
 */
object First {

  def main(args: Array[String]): Unit = {
    //写spark代码
    //1.创建spark Context，定义为变量sc

    //通过spark提供的算子完成业务的处理
    //a.通过textFile加载文件；
    //b.通过flatMap/map/groupBy/算子完成计算处理
    //c.collect

    //n.sc.stop();//是否资源

    //需要先创建一个sparkConf(配置)，至少需要配置master、以及appName
    //在创建sc的时候，需要记载sparkConf

    /*val sparkConf: SparkConf = new SparkConf()
    sparkConf.setMaster("local")//在配置中设置master;如果本地运行可以写local
    sparkConf.setAppName("suibian写")*/

    //上面的代码可以做一个简化处理
    val sparkConf: SparkConf = new SparkConf()
      .setMaster("yarn")
//      .setMaster("spark://hadoop10:7077")
//      .setMaster("local[*]")//支持本地运行，支持yarn、standalone
      //本地运行可以写local==》启用一个executor
      //local[n]==》启用n个executor
      //local[*]==》cpu由多少核，就启用多少个executor

      //要部署到spark服务器中，
      // 可以写spark://spark_host:spark_port===>通过standalone模式运行
      // 可以写yarn===》通过yarn资源管理系统完成程序的运行
      .setAppName("first")


    val sc: SparkContext = new SparkContext(sparkConf)

    val rdd: RDD[String] = sc.textFile("hdfs://hadoop10:9000/a.txt")

    /*val strings: Array[String] = rdd.collect()

    println(strings)*/

    val array: Array[(String, Int)] = rdd
      .flatMap(_.split("\\s+")) //可以匹配多个空格（至少一个）
      .map((_, 1))
      .groupBy(_._1)
      .map(e => (e._1, e._2.size))
      .collect()

    //foreach是Scala的算子，跟spark没有任何关系
    array.foreach(println(_))

    //
    sc.stop()


  }

}
