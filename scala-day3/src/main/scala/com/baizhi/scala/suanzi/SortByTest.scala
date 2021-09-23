package com.baizhi.scala.suanzi

object SortByTest {
  def main(args: Array[String]): Unit = {
    //list中的每一个元素是一个元组
    //在元组中，第一个元素表示的是考生姓名；第二个元素表示的是考试成绩
    var list = List(("b",9),("q",5),("s",8),("a",6))

    //要按照考试成绩进行升序排列
    /*val newList: List[(String, Int)] = list.sorted
    for (elem <- newList) {
      println(elem)
    }*/
    val newList: List[(String, Int)] = list.sortBy(_._2)
    /*for (elem <- newList) {
      println(elem)
    }*/

    //要按照考试成绩做降序排列
    val newList2: List[(String, Int)] = list.sortBy(_._2).reverse
    for (elem <- newList2) {
      println(elem)
    }
  }

}
