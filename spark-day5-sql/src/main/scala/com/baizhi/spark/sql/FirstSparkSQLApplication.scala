package com.baizhi.spark.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

object FirstSparkSQLApplication {

  def main(args: Array[String]): Unit = {
    //1.sparkSession对象的创建
    //2.读取数据源
    //形成df
    //基于df中数据生成一个表：临时表

    //写SQL

    //通过sparkSQL计算引擎完成SQL的执行
    //把结果写出去：打印、写mysql

    //n.sparkSession.stop//释放资源


//    new SparkSession()//这种写法不行；因为所有的构造器都是私有的

    val spark: SparkSession = SparkSession
      .builder()//Object SparkSession里面的一个方法，用来创建SparkSession.Builder对象
      .master("local[*]")//设置master
      .appName("firstSQLApp")//设置AppName
      .getOrCreate()//通过SparkSession.Builder对象的getOrCreate方法获取到一个SparkSession对象


    //加载hdfs中的数据
    val rdd: RDD[String] = spark.sparkContext.textFile("hdfs://hadoop10:9000/stu.txt")
//    rdd.foreach(str=> println(str))

    //toDF,需要隐式转换
    import spark.implicits._//spark是上面定义的变量
//    val df: DataFrame = rdd.toDF()//默认的列名叫value
//    val df: DataFrame = rdd.toDF("id", "name", "age")//异常，因为rdd只有一列，转换成df生成3列

    val rdd2: RDD[Array[String]] = rdd.map(str => str.split("\\s+"))
    val rdd3: RDD[(Int, String, Int)] = rdd2.map(array => (array(0).toInt, array(1), array(2).toInt))
    val df: DataFrame = rdd3.toDF("id", "name", "age")//里面的参数就是列名，随便写的

//    df.show()//在控制台显示df中的数据信息

    //基于df中的数据信息，形成一张表
    df.createTempView("t_stu")//t_stu是一个表名，随便写的
    var sql=
      """
        |select * from t_stu
        |order by age
        |""".stripMargin

    val result: DataFrame = spark.sql(sql)//spark-sql引擎执行SQL语句

    result.show()



    spark.stop()//释放资源

  }

}
