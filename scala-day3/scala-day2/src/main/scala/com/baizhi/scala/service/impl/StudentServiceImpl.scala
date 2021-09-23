package com.baizhi.scala.service.impl

import com.baizhi.scala.entity.{Person, Student}
import com.baizhi.scala.service.{S, StudentService}

class StudentServiceImpl extends Person with StudentService {
  override def update(stud: Student): Unit = {


  }

  //通过ctrl+O快捷键把需要实现的默认方法调出来
  override def add(student: Student): Unit = {


  }


}
