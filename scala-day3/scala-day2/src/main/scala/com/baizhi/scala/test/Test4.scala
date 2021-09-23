package com.baizhi.scala.test

import com.baizhi.scala.bansheng.Cat

object Test4 {

  def main(args: Array[String]): Unit = {
    /*var cat:Cat=new Cat

    cat.eat()*/

    //伴生对象中添加了apply方法，就可以快速创建对象
    /*var cat:Cat=Cat()//就是执行了apply方法创建的对象

    cat.eat()*/
    var t=Test3
    var t2=Test3
    println(t==t2)


//    var array:Array[Int]=Array()
  }

}
