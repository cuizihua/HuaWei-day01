package com.baizhi.scala.suanzi

object ForeachTest {

  def main(args: Array[String]): Unit = {
    val list=List("a","b","c","d")

    //foreach算子，就是执行foreach循环的
    list.foreach(e=>{
      println(e)
    })
  }

}
