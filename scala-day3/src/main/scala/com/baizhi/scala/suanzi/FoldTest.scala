package com.baizhi.scala.suanzi

object FoldTest {

  def main(args: Array[String]): Unit = {
    var list=List(1,2,3,4,5)

    //fold,和reduce算子一样，都是聚合函数
    //fold需要一个初始值，然后会把这个初始值赋值给v1
    //取list集合中的每一个元素赋值给v2进入到函数体执行运算
    //运算完成之后，赋值给v1
    val sum: Int = list.fold(10)((v1, v2) => {
      println(v1 + "&&" + v2)
      v1 + v2
    })

    println(sum)
  }

}
