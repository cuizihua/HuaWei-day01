package com.baizhi.scala.implicit_

object Test {

  def main(args: Array[String]): Unit = {
    //隐式函数
   /*implicit def double2Int(num:Double):Int={
     println("***********")
      num.toInt+10
    }

//    var n:Int=double2Int(1.7)

    var n:Int=1.7//其实就是调用了上面的函数
    println(n)*/

    /*//隐式变量
    implicit var name:String="张三"

    //函数的形参也声明成隐式变量
    def hello(implicit name:String="abc"):Unit={
      println("hello,"+name)
    }

//    hello("zhangsna")
    hello//在调用hello函数，不需要传递值，会使用隐式变量的值*/

    implicit class A(b:B){
      def testA(): Unit ={
        println("testA method")
      }
    }

    var b:B=new B
//    b.testB()

    b.testA()

  }

}

//普通的类
class B{
  def testB()={
    println("testB method")
  }
}
