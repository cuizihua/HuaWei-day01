package com.baizhi.spark.udaf

import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, IntegerType, StructType}

/**
 * 自定义udaf：求平均值
 *
 * 求员工的平均年龄
 *
 */
object TestApplication {

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("udfApp")
      .getOrCreate()

    import spark.implicits._

    val list: List[(Int, String, Int)] = List((1, "zs", 20), (2, "li", 22), (3, "ls", 19))

    val df: DataFrame = list.toDF("id", "name", "salary")

    df.createTempView("t_emp")


    spark.udf.register("salary_avg",new MyUDAF)


    var sql=
      """
        |select round(salary_avg(salary),2) from t_emp
        |""".stripMargin

    val result: DataFrame = spark.sql(sql)
    result.show()

    spark.stop()

  }

}

//1.定义一个类，继承UserDefinedAggregateFunction
//2.实现里面所有的方法
class MyUDAF extends UserDefinedAggregateFunction{

  /**
   * 输入数据的类型
   * @return
   */
  override def inputSchema: StructType = new StructType().add("doubleInput", IntegerType)

  /**
   * 中间计算过程的数据类型
   * ageSum表示年龄总和
   * countSum表示人数总和
   * @return
   */
  override def bufferSchema: StructType = {

    new StructType()
      .add("ageSum", IntegerType)
      .add("countSum", IntegerType)
  }

  /**
   * 自定义udaf函数的返回值类型
   * @return
   */
  override def dataType: DataType = DoubleType

  /**
   * 这个函数返回true，表示相同的输入总能获取到相同的输出
   * @return
   */
  override def deterministic: Boolean = true

  /**
   * 对数据进行初始化
   * @param buffer
   */
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0)=0//年龄总和的初始值
    buffer(1)=0//人数总和的初始值
  }

  /**
   * 更新buffer：
   * 在使用这个自定义函数的时候，每读取到一行数据都会执行一次这个函数
   * @param buffer 缓冲区数据
   * @param input 输入数据
   */
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0)=buffer(0).toString.toInt+input(0).toString.toInt//处理年龄的
    buffer(1)=buffer(1).toString.toInt+1//处理数量
  }

  /**
   * 合并缓冲区
   * @param buffer1
   * @param buffer2
   */
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0)=buffer1(0).toString.toInt+buffer2(0).toString.toInt
    buffer1(1)=buffer1(1).toString.toInt+buffer2(1).toString.toInt
  }

  /**
   * 把最终的缓冲区的数据进行计算处理，返回使用函数的地方
   * @param buffer
   * @return
   */
  override def evaluate(buffer: Row): Any = buffer(0).toString.toDouble/buffer(1).toString.toInt
}
