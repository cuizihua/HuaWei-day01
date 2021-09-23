package com.baizhi.scala.suanzi

object FlattenTest {

  def main(args: Array[String]): Unit = {
    //创建一个集合，集合中的每一个元素是集合
    var list=List(List(1,2,34),List(3,4,5))

    //flatten，就是把list集合中的每一个元素（集合）中的元素获取到形成新的集合
    val newList: List[Int] = list.flatten

    for(e<-newList){
      println(e)
    }
  }

}
