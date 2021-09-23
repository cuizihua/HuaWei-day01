package com.baizhi.scala.entity

class Student  extends Person {
  override def eat(food: String): Unit = {
    println("s")
    super.eat(food)//
  }
}
