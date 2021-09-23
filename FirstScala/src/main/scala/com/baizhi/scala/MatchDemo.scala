package com.baizhi.scala

object MatchDemo {

  def main(args: Array[String]): Unit = {
    var str:String = "C"

    //通过match：模式匹配的方式，实现Java中的switch-case的效果
    /*
    语法结构
    变量 match{
     case 值1 => 当变量的值是值1的时候，执行这里的代码
     case 值2 => 当变量的值是值2的时候，执行这里的代码
     case _ => 当变量的值没有匹配上面所有值时，执行这里的代码
    }
     */

    str match {
      case "a" =>println("1")
      case "A" =>println("2")
      case "B" =>println("C")
      case "D" =>println("ED")
      case _ =>println("default")
      case "C" =>println("D")
      case "abc" =>{
        println("没有以上")
        println("每一私钥")
        println("还没有写对")
        println("这一次写对啦")
      }
    }
  }

}
