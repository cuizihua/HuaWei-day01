package com.baizhi.spark.dsl

import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object TestApplication {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("dslApp")
      .getOrCreate()

    import spark.implicits._
    val list: List[(Int, String, Int)] = List((101, "n", 23), (102, "nn", 22))

    val df: DataFrame = list.toDF("id", "name", "age")

    //DSL：领域特定语言；针对某一个具体的事项实现一种特殊写法
    val result: DataFrame = df.select("name", "age")//select name,age from table_name
//    result.show()

    val ds: Dataset[Row] = df.filter(row => row(2).toString.toInt > 22)//select * from table_name where age>22
    ds.show()


    spark.stop()
  }

}
