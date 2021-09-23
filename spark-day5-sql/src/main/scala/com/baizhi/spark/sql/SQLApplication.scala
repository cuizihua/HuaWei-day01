package com.baizhi.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

object SQLApplication {

  def main(args: Array[String]): Unit = {
    //1.创建sparkSession
    val spark: SparkSession = SparkSession
      .builder()
      .config("spark.app.name","SQLApp")
      .config("spark.master","local[*]")
      .getOrCreate()
    //隐式转换导入进来
    import spark.implicits._

    val list: List[(Int, String, Int)] = List((1, "张三", 20), (2, "李四", 30), (3, "李s", 25), (4, "李ss", 30))

    //DataFrame是一个没有类型的数据类型
    val df: DataFrame = list.toDF("id", "name", "age")

    //生成临时视图
    df.createTempView("t_person")

    //基础SQL
    /*var sql=
      """
        |select * from t_person
        |""".stripMargin*/

    //条件查询
    /*var sql=
      """
        |select * from t_person
        |where id=1
        |""".stripMargin*/

    //模糊查询
    /*var sql=
      """
        |select * from t_person
        |where name like '%张%'
        |""".stripMargin*/

    //排序
    /*var sql=
      """
        |select * from t_person
        |order by age desc,id
        |""".stripMargin*/

    //limit
    /*var sql=
      """
        |select * from t_person
        |limit 2
        |""".stripMargin*/
    //获取年龄最大的两个用户信息

   /* var sql=
      """
        |select * from t_person
        |order by age desc
        |limit 2
        |""".stripMargin*/


    //distinct关键字的使用
    //获取所有人的年龄信息
    /*var sql=
    """
      |select distinct age from t_person
      |""".stripMargin*/

    //group by的应用
    //统计每一个部门中有多少员工
    /*val df2: DataFrame = List((1, "zs", 1), (2, "lis", 2), (3, "ww", 2)).toDF("id", "name", "dept_id")
    df2.createTempView("t_dept")
    var sql=
      """
        |select dept_id,count(*) from t_dept
        |group by  dept_id
        |""".stripMargin*/


    //case when then
    //当字段的值是某一个值的时候，需要转换成一个对应的值
    //当性别是0的时候，需要转换成女
    //case 字段 when 原本的值 then 需要显示的值

    //case when then else end
    //case sex when 0 then 女 else 男 end
    val df2: DataFrame = List((1, "zs", 1), (2, "ls", 0), (3, "xhh", 1)).toDF("id", "name", "sex")
    df2.createTempView("t_stu")

    /*var sql=
      """
        |select id,name,sex from t_stu
        |""".stripMargin*/
    /*var sql=
      """
        |select id,name,
        | case sex when 0 then '女' else  '男' end sex
        | from t_stu
        |""".stripMargin*/


    //行列转换的sql
    val df3: DataFrame = List((1, 1, "语文", 145), (2, 1, "数学", 150), (3, 1, "英语", 148), (4, 2, "语文", 76), (5, 2, "数学", 50), (6, 2, "英语", 48))
      .toDF("id", "stu_id", "subject", "score")
    df3.createTempView("t_score")

   /* var sql=
      """
        |select * from t_score
        |""".stripMargin*/

    /*var sql =
      """
        | select stu_id,
        | max(case subject when '语文' then score else 0 end )chinese,
        | max(case subject when '数学' then score else 0 end )math,
        | max(case subject when '英语' then score else 0 end )english
        | from t_score
        |
        | group by stu_id
        |""".stripMargin*/


    val stuDF = List((1, "张三", 1001), (2, "李四", 1001), (3, "王五", 1002), (3, "赵六", 1003))
      .toDF("id","name","cid")

    val classDF = List((1001, "Java班"), (1002, "UI班"))
      .toDF("cid","cname")

    stuDF.createOrReplaceTempView("t_student")
    classDF.createOrReplaceTempView("t_class")

    //统计学生姓名以及所在班级名称
    /*var sql=
      """
        |select t1.name,t2.cname  from t_student t1 inner join t_class t2 on t1.cid=t2.cid
        |""".stripMargin*/

    /*var sql =
      """
        |select t1.name,t2.cname from t_student t1,t_class t2 where t1.cid=t2.cid
        |""".stripMargin*/

    var df5:DataFrame=List((7,"zss",true,1,15000),(1,"zs",true,1,15000),(2,"ls",false,2,18000)
      ,(6,"lss",false,2,18000),(3,"ww",false,2,14000),(4,"zl",false,1,18000),
      (5,"win7",false,1,16000)).toDF("id","name","sex","dept","salary")

    df5.createOrReplaceTempView("t_emp")
    //按照工资进行排序
    /*var sql=
      """
        |select * from t_emp order by salary
        |""".stripMargin*/

    //在部门内部，按照工资排序
    //row_number():连续的顺序数字；内容一样排名不一样
    /*var sql=
      """
        |select id,name,dept,salary, row_number() over(partition by dept order by salary) rn from t_emp
        |""".stripMargin*/

    //rank():不连续的数字；内容一样排名一样
    /*var sql=
    """
      |select id,name,dept,salary, rank() over(partition by dept order by salary) rn from t_emp
      |""".stripMargin*/

    //dense_rank():连续的数字；内容一样排名一样
    var sql=
      """
        |select id,name,dept,salary, dense_rank() over(partition by dept order by salary) rn from t_emp
        |""".stripMargin


    val result: DataFrame = spark.sql(sql)

    result.show()

    spark.stop()
  }

}
