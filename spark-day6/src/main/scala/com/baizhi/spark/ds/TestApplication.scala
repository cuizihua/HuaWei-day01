package com.baizhi.spark.ds

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object TestApplication {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("visitApp")
      .getOrCreate()

    import spark.implicits._
    val list2: List[(Int, String, Int)] = List((101, "n", 23), (102, "nn", 22))
    val df3: DataFrame = list2.toDF()
    df3.show()

    /*val list: List[(Int, String, Int)] = List((101, "n", 23), (102, "nn", 22))


    val df: DataFrame = list.toDF("id", "name", "age")
    val ds: Dataset[(Int, String, Int)] = list.toDS()
    ds.show()*/

    val list: List[Person] = List(new Person(101, "n", 23), new Person(102, "nn", 22))
    val df: DataFrame = list.toDF()
    val ds: Dataset[Person] = list.toDS()

    val rdd: RDD[Row] = df.rdd
    val rdd1: RDD[Person] = ds.rdd

    val ds2: Dataset[Person] = df.as[Person]
    val df2: DataFrame = ds.toDF()

//    df.show()
//    ds.show()

    spark.stop()

  }

}

case class Person(id:Int,name:String,age:Int)
