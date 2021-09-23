package com.baizhi.scala.implicit_

object MyImplicit {

  implicit def double2Int(num:Double):Int=num.toInt

}
