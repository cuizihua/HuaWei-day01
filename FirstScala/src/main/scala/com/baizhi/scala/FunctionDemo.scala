package com.baizhi.scala

object FunctionDemo {

  def main(args: Array[String]): Unit = {
    /*val num: Int = fun2(5,1)
    println(num)*/

    /*val i: Int = fun3(4)
    print(i)*/

    /*val i: Int = fun3(7)
    println(i)*/

   /* val i: Int = fun4()
    val ff: Int = fun4(4)
    val fff: Int = fun4(4,4)*/

    /*lazy val num: Int = fun6()//有了lazy，在使用num这个变量的时候，才执行fun6
    println("****************")
    println(num)*/

    /*def fun7():Unit={
      println("(")
    }

    fun7()*/


   var ff=(num1:Int,num2:Int)=>num1+num2

     /*val sum: Int = f(1, 2)
    println(sum)*/

//    val sum: Int = fun9(ff, 3, 4)
//    println(sum)
//    val sum: Int = fun9((num1:Int,num2:Int)=>num1+num2, 3, 4)

    //调用fun10
    val f: (Int, Double) => Double = fun10()

    val d: Double = f(1, 12)

  }

  def fun():Unit={
    println("********")
  }

  def fun1():Int={
    println("^^^^^")
//    return 1

    1
  }

  def fun2(num1:Int,num2:Int):Int={
    num1+num2
  }

  //参数可以给默认值
  //函数参数有了默认值之后，在调用函数的时候，如果没有传值，用的就是默认值
  //如果传递了值，就会把默认值覆盖掉
  def fun3(num1:Int=5):Int={
    num1+1
  }

  def fun4(num:Int*):Int={

    5
  }

  def fun5():Int=4 //当函数体只有一行的时候，建议大括号省略不写

  def fun6():Int={
    println("&&&&")
    6
  }

  //返回值类型可以省略
  def fun8()={

  }

  //def/ 函数名省略===》匿名函数
  /*(num:Int,num2:Int):Int=>2*/

  /**
   *
   * @param f 是一个函数类型，这个函数需要两个Int类型的参数，执行完成之后，需要返回一个int类型的值
   * @param num1
   * @param num2
   * @return
   */
  def fun9(f:(Int,Int)=>Int,num1:Int,num2:Int):Int={

    println(num1)//使用num1

    //使用f
    f(num1,num2)

  }

  def fun10():(Int,Double)=>Double = {
    (num1:Int,num2:Double)=>num2+num1
  }


}
