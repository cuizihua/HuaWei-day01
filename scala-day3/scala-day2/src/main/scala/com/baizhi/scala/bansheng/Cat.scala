package com.baizhi.scala.bansheng

//伴生类
class Cat {

  def eat():Unit={
    println("小猫在吃鱼")
  }

}

//伴生对象
object Cat{

  //通过伴生对象的apply方法，可以完成对象的创建
  def apply(): Cat = {
    println("apply方法的使用")
    new Cat()
  }
}
