package com.baizhi.spark.function

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
 * 访问量统计
 * 某App收集用户的日志，做对应的处理，已经按天统计了用户的访问次数。就形成了如下数据
 * 用户编号     日期       访问次数
 * 1        2021/6/12       12
 * 1        2021/6/13       120
 * 1        2021/6/15       8
 * 2        2021/6/12       1
 * 2        2021/6/14       1200
 * 1        2020/7/12       112
 * 1        2020/7/13       110
 * 1        2020/7/15       18
 * 2        2021/5/12       1
 * 2        2021/5/14       1200
 *
 * 需要完成的功能，
 * 1.按月统计用户的总访问量
 * 2.还需要一列，到这个月为止，用户总的访问量
 */
object VisitApplication {

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("visitApp")
      .getOrCreate()

    import spark.implicits._

    val rdd: RDD[String] = spark.sparkContext.textFile("e:/a.log")
    val df: DataFrame = rdd
      .map(_.split("\\s+"))
      //.foreach(array=>println(array.mkString(",")))
      .map(array => (array(1).toInt, array(2).substring(0, array(2).lastIndexOf("/")), array(3).toInt))
      //        .foreach(t=> println(t._2))
      .toDF("id", "date", "count")
//    df.show()

    df.createTempView("t_visit_log")

    val sql1=
      """
        |select id,date,sum(count) sum from t_visit_log
        |group by id,date
        |order by id,date
        |""".stripMargin

    val result: DataFrame = spark.sql(sql1)

    //实现第二个功能
    result.createTempView("t_tmp")
    result.show()


    var sql2=
      """
        |select id,date,sum,sum(sum) over(partition by id order by date) sum2 from t_tmp
        |""".stripMargin

    val result2: DataFrame = spark.sql(sql2)

    result2.show()





    spark.stop()

  }

}
