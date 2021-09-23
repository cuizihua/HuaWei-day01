package com.baizhi.scala.service

import com.baizhi.scala.entity.Student

//Scala中的接口
trait StudentService {

  //scala基于JDK1.8版本后，所有的方法都可以由默认实现
  def add(student:Student):Unit={}

  //接口中定义的抽象方法
  def update(stud:Student)

}
