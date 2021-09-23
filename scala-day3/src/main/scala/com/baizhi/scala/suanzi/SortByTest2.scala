package com.baizhi.scala.suanzi

import scala.math.Ordering

/**
 * 现在正在高考
 * 通过list集合表示考试的高考成绩（语文、数学、英语、综合）
 * List((姓名,语文成绩,数学成绩,英语成绩,综合成绩))
 *
 * 放榜：按照语文成绩、综合成绩、英语成绩、数学成绩的升序排列
 */
object SortByTest2 {
  def main2(args: Array[String]): Unit = {
    var list = List(("A",135,150,146,298),("B",140,99,150,99),("C",135,140,150,180))

    //当有多个排序规则的时候，把多个排序规则封装形成元组

    val newList: List[(String, Int, Int, Int, Int)] = list.sortBy(t => (t._2, t._5, t._4, t._3))

    //如果写了reverse，就会把所有的排序规则都做降序处理
//    val newList: List[(String, Int, Int, Int, Int)] = list.sortBy(t => (t._2, t._5, t._4, t._3)).reverse

    for (elem <- newList ) {
      println(elem)
    }
  }

  def main(args: Array[String]): Unit = {
    //（姓名，语文成绩，英语成绩）
    val list=List(("A",135,140),("B",135,110),("C",140,145),("D",140,150))

    //需求：按照语文成绩降序排列，如果语文成绩一样，按照英语成绩升序排列

    //sortBy算子其实需要两个参数sortBy(f)(Ordering类型的参数)
    //Ordering类型的参数是隐式变量，所以当默认的隐式变量值能满足要求的时候，可以可以不用写
    //当默认的隐式变量值不能满足要求的时候，
    // 就应该创建对应的Ordering对象（第一个参数f,返回值是两个元素的元组，这里就应该创建Ordering.Tuple2）
    val newList: List[(String, Int, Int)] = list
      .sortBy(t => (t._2, t._3))(Ordering.Tuple2(Ordering.Int.reverse, Ordering.Int))
    for (elem <- newList) {
      println(elem)
    }
  }

}
