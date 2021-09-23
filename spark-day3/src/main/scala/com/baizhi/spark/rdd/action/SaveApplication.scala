package com.baizhi.spark.rdd.action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * saveAsTextFile:以文本的方式保存数据
 * saveAsSequenceFile:以hadoop sequenceFile的格式化保存:针对k-v结构的数据进行保存
 * saveAsObjectFile：对象的序列化，把对象存储起来
 */
object SaveApplication {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("saveApp").setMaster("local[*]")

    val sc: SparkContext = new SparkContext(conf)


    val rdd: RDD[Int] = sc.makeRDD(1 to 10)

//    rdd.saveAsTextFile("d:/a.txt")
    //rdd.saveAsTextFile(directory)//把rdd中数据写入到这个目录下

    /*val rdd2: RDD[String] = sc.textFile("d:/a.txt")//到a.txt这个目录中读取数据

    val array: Array[String] = rdd2.collect()
    println(array.mkString(","))*/
//    rdd.saveAsObjectFile("f:/obj")

    val rdd3: RDD[(String, Int)] = sc.makeRDD(List(("a", 1), ("b", 2), ("a", 3)))
//    rdd3.saveAsSequenceFile("e:/seq")
//    rdd3.saveAsObjectFile("f:/obj2")

//    val list: List[Person] = List(new Person("z", 22), new Person("l", 18))
//    val rdd5: RDD[Person] = sc.makeRDD(list)
//
//    rdd5.saveAsObjectFile("f:/obj3")

    val rdd6: RDD[Person] = sc.objectFile("f:/obj3")
    println(rdd6.collect().mkString(","))

    sc.stop()


  }

}

case class Person(name:String,age:Int)
