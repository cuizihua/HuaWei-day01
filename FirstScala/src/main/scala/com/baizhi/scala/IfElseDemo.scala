package com.baizhi.scala

import scala.io.StdIn

/**
 * 如果输入的数字是0，就显示女；
 * 如果输入的数字是1，就显示男；
 * 否则，就显示“你是不是在开玩笑”
 */
object IfElseDemo {


  def main(args: Array[String]): Unit = {
    println("请输入一个数字：")
    val num: Int = StdIn.readInt()
    /*if(num==1){
      println("男")
    }else if(num==0){
      println("女")
    }else{
      println("你是不是在开玩笑")
    }*/
    /*var sex:String=if(num==1){
      "男"
    }else if(num==0){
      "女"
    }else{
      "你是不是在开玩笑"
    }*/


    var sex:String=if(num==1) "男" else if(num==0) "女" else "你是不是在开玩笑"
    println(sex)
    var a :Int=5
    a=6

    val b:Int=6
    b=7

  }

}
