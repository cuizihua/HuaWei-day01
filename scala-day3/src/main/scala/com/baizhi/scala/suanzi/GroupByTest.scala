package com.baizhi.scala.suanzi

object GroupByTest {

  def main(args: Array[String]): Unit = {
    val list=List(("A",90),("A",99),("A",98),("B",99),("B",88),("B",81))

    //根据名字进行分组处理
    val map: Map[String, List[(String, Int)]] = list.groupBy(_._1)

    println(map)
  }

}
