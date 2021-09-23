package com.baizhi.spark.df

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

object TestApplication {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("visitApp")
      .getOrCreate()

    import spark.implicits._

    val df: DataFrame = List((101, "n", 23), (102, "nn", 22)).toDF("id", "name", "age")

    df.printSchema()//

    val rdd: RDD[Row] = df.rdd
//    rdd.foreach(row=> println(row))
//val rdd2: RDD[(Any, Any, Any)] = rdd.map(row => (row(0), row(1), row(2)))
val rdd2: RDD[(Int, String, Int)] = rdd.map(row => (row(0).toString.toInt, row(1).toString, row(2).toString.toInt))
    rdd2.foreach(t=> println(t))

    spark.stop()
  }

}
