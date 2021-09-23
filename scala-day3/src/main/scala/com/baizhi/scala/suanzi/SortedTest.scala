package com.baizhi.scala.suanzi

object SortedTest {

  def main(args: Array[String]): Unit = {
    var list=List(3,4,5,2,1,6,9,11,8)

    for (elem <- list) {
      print(elem+" ")
    }

    println("\n***************************************")
    //sorted就是用来排序的，默认按照升序排列
    val newList: List[Int] = list.sorted
    for (elem <- newList) {
      print(elem+" ")
    }

    println()
    println()
    //要按照降序排列
    //reverse:反转的意思
    val newList2: List[Int] = list.sorted.reverse
    for (elem <- newList2) {
      print(elem+" ")
    }
  }

}
