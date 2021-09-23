package com.baizhi.scala


object Second {
  def main(args: Array[String]): Unit = {

    println("请输入一个整数：")

    import scala.io.StdIn//可以把导包作为一行代码，写在代码里面
    val num: Int = StdIn.readInt()

    println(num)

  }
}
