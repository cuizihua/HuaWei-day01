package com.baizhi.spark.rdd.share

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

object AccumulatorApplication {

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("accumulatorApp").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)


    val rdd: RDD[Int] = sc.makeRDD(1 to 10)

    var num:Int=10//diver端

//    rdd.foreach(e=>num+=e)//并行计算、executor上计算
    rdd.foreach(e=>{
      num+=e
//      println("num="+num)//
      //上面的这一行代码，小括号里面是字符串的拼接
      //在scala中，字符串的拼接还可以有更简单的写法
      println(s"num=${num},e=${e}")

      //字符串还可以这样
      /*var s:String=
        """
          |sdf
          |sdf
          |s
          |f
          |sd
          |f
          |s
          |df
          |sf
          |s
          |df
          |
          |""".stripMargin*/
    })

    println(num)//获取不到executor上计算的内容

    //共享变量：累加器
    //在diver端定义一个累加器，在executor里面使用累加器的值执行计算处理
    //在diver端获取到累加器中的新的值

    val rdd3: RDD[Int] = sc.makeRDD(1 to 10)

    val longAccumulator: LongAccumulator = sc.longAccumulator("myAccumulator")

    rdd3.foreach(e=>{
      longAccumulator.add(e)//取e的值累加到longAccumulator上面
    })

    println(longAccumulator.value)//获取到累加器中的值

    sc.stop()
  }

}
