package com.baizhi.spark.loadsave

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object TestApplication {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("udfApp")
      .getOrCreate()

    import spark.implicits._

    val list: List[(Int, String, Int)] = List((1, "zs", 23), (2, "ls", 22))
    val df: DataFrame = list.toDF("id", "name", "age")

//    df.write.json("d:/a.json")

    /*val df2: DataFrame = spark.read.json("d:/a.json")
    df2.show()*/

    var url="jdbc:mysql://localhost:3306/test?useUnicode=true&charactorEncoding=utf8"
    var table="t_user"
    var connectionProperties:Properties=new Properties()
    connectionProperties.setProperty("user","root")
    connectionProperties.setProperty("password","root")
    df.write.mode(SaveMode.Append).jdbc(url,table,connectionProperties)

    /*val df4: DataFrame = spark.read.jdbc(url, table, connectionProperties)
    df4.show()*/


    spark.stop()

  }

}
