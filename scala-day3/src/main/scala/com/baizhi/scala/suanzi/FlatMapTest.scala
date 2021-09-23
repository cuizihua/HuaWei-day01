package com.baizhi.scala.suanzi

object FlatMapTest {

  def main(args: Array[String]): Unit = {
    var list=List("mr hdfs","hive scala sqoop flume","kafka zk azkaban")


    //list.map(_.split(" "))
    /*val newList: List[Array[String]] = list.map(_.split(" "))
    val newList3: List[String] = newList.flatten
    for(e<-newList3){
      println(e)
    }*/

    val newList: List[String] = list.flatMap(_.split(" "))
    for(e<-newList){
      println(e)
    }

    //总结
    //map:把每一个元素做对应的映射处理。一个集合使用map，
    // 生成了另一个集合，两个集合中的元素个数一样

    //flatMap:把每一个元素做对应的映射处理，同时执行flatten。
    // 生成新的集合，新的集合和原来的集合元素个数可以不一样

  }

}
