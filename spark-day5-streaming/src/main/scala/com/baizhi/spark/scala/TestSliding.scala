package com.baizhi.spark.scala

object TestSliding {

  def main(args: Array[String]): Unit = {
    val list: List[Int] = List(1, 2, 3, 4, 5, 6, 7, 8)


    //通过scala中的窗口理解一下窗口的概念
    //sliding:滑动窗口，
    // 一个是窗口大小：用来控制窗口中的元素个数，
    // 一个是滑动步长：用来控制当前窗口和下一个窗口对应的是否重叠
    //两个参数：size：窗口大小，sliding：滑动步长
    //list.sliding(size,step)
    //size:窗口大小，就上面的代码来讲，
    // 设置了窗口大小，就会把list集合中的元素按照这个大小进行切分
    // step：滑动步长

    val iterator: Iterator[List[Int]] = list.sliding(3, 3)
    iterator.foreach(list=> println(list.mkString(",")))



  }

}
