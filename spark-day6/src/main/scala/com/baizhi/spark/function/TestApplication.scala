package com.baizhi.spark.function

import org.apache.spark.sql.{DataFrame, SparkSession}

object TestApplication {
  def main(args: Array[String]): Unit = {
    //1.创建sparkSession
    //2.导入隐式转换
    //3.加载数据
    // 需要把数据生成一个临时视图
    //4.写sql
    //5.执行sql，获取到对应的结果
    //6.把结果保存
    //释放资源

    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("functionApp")
      .getOrCreate()

    import spark.implicits._
    val df: DataFrame = List(
      (1, "zs", true, 1, 100),
      (2, "ls", false, 2, 300),
      (3, "ww", false, 2, 500),
      (4, "zl", false, 1, 200),
      (5, "win7", false, 1, 150)
    ).toDF("id", "name", "sex", "dept", "salary")

    df.createTempView("t_emp")

    //统计整个公司，一个月的工资开支
    /*var sql=
      """
        | select sum(salary) from t_emp
        |""".stripMargin
    val result: DataFrame = spark.sql(sql)*/


    //统计每一个部门的工资总和
    //group by完成统计，在查询中只能写group by后的字段名
    /*val sql=
      """
        |select dept, sum(salary) sum from t_emp
        |group by dept
        |""".stripMargin*/

    //通过开窗函数完成部门工资总和的统计
    //通过开窗函数完成，可以在查询中写所有的字段
    /*val sql=
      """
        |select id,name,sex,salary,dept, sum(salary) over(partition by dept) sum from t_emp
        |""".stripMargin

    val result: DataFrame = spark.sql(sql)*/

    //over里面有order by之后的结果：做累加处理
    //over里面没有partition by就会对所有的数据进行累加处理
   /* val sql=
      """
        |select id,name,sex,salary,dept, sum(salary) over(order by salary) sum from t_emp
        |""".stripMargin*/
    //over里面有partition by，就会根据分组字段分别做累加处理
    val sql=
      """
        |select id,name,sex,salary,dept, sum(salary) over(partition by dept order by salary) sum from t_emp
        |""".stripMargin


    val result: DataFrame = spark.sql(sql)

    result.show()



    spark.stop()


  }

}
