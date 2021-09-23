package com.baizhi.spark.udf

import org.apache.spark.sql.{DataFrame, SparkSession}

object TestApplication {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("udfApp")
      .getOrCreate()

    import spark.implicits._

    val list: List[(Int, String, Int)] = List((101, "zs", 1), (102, "ls", 0))

    val df: DataFrame = list.toDF("id", "name", "sex")

    df.createTempView("t_user")


    /*var sql=
      """
        |select id,name,sex from t_user
        |""".stripMargin*/
    var f1=(sex:Int)=>{
      sex match {
        case 1=>"男"
        case 0=>"女"
        case _=>"性别未知"
      }
    }
    spark.udf.register("convert_sex",f1)

    var sql=
      """
        |select id,name,convert_sex(sex) sex from t_user
        |""".stripMargin

    val result: DataFrame = spark.sql(sql)
    result.show()


    spark.stop()
  }

}
