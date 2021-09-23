package com.baizhi.scala.gaoji

object BiBao {

  def main(args: Array[String]): Unit = {
    //声明一个函数，这个函数的返回值是一个函数
    //(num2:Int)=>num1+num2是sum这个函数的返回值
    def sum(num1:Int)=(num2:Int)=>num1+num2

    var num1:Int=3
    var f=sum(num1)//调用sum这个函数;f是一个函数，f就是闭包

    val s: Int = f(4)
    println(s)

    //可以这样写
    val s2: Int = sum(3)(4)
    println(s2)

    //基于上面的代码可以定义一个函数
    //函数柯里化:所有的函数都可以转换成一个参数的函数
    def sum2(num1:Int)(num2:Int)=num1+num2

    val s4: Int = sum2(3)(4)

    //普通函数的声明
    def sum3(num1:Int,num2:Int)=num1+num2

  }

}
