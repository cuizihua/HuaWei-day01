package com.baizhi.scala.iterable_

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Test {

  def main(args: Array[String]): Unit = {
    /*var arr:Array[Int]=new Array[Int](9)//
    var arr1:Array[Int]=Array(1,2,3,4)//伴生对象的使用，使用的是对应的apply方法

    //for循环遍历数组
    for(index <- 0 until  arr1.length){
      arr1(index)//通过下标获取到数组中的元素
    }

    //通过增强型for循环==foreach循环
    //在这个代码中，e就是声明的一个变量，可以随便写
    for(e <- arr1){
      println(e+"**")
    }*/

   /* val strings: ArrayBuffer[Int] = new ArrayBuffer[Int]()
    strings+=2//添加一个元素

    var str:ArrayBuffer[String]=new ArrayBuffer[String]()
    str+="a"//添加一个元素

    //toArray方法，把可变数组转换成定长数组
    val array: Array[String] = str.toArray

    //toBuffer,可以把定长数组，转换成一个buffer，基于buffer完成对数据的添加以及删除
    val buffer: mutable.Buffer[String] = array.toBuffer*/

    /*var tuple=(1,"a",true,1.2)//定义一个元组
    val value: Int = tuple._1//获取到元组中的第一个元素
    val iterator: Iterator[Any] = tuple.productIterator
    for(e<-iterator){
      println(e)
    }

    //应用
    //word count
    //(word,1)*/

    /*var list:List[Int]=List(10,2,3,5,6)//创建一个集合
    var list2=List()//创建一个空集合
    var list3=Nil//创建一个空集合，和上面的代码一样

    //以下标的方式获取数据
    val first: Int = list(0)
//    println(first)

    //for
    /*for(index <- 0 until list.size){
      println(list(index))
    }*/

    //foreach循环
    /*for(e <- list){
      println(e)
    }*/

    val list4: List[Int] = list2 :+ 11//往list2中添加一个元素11
    /*for(e <- list4){
      println(e)
    }*/

    val list5: List[Int] = 111 +: list2//往list2中添加一个元素111

    val list7: List[Int] = 14 :: list2//往list2中添加一个元素14
    /*for(e <- list7){
          println(e)
        }*/

    val list9: List[Int] = list ::: list2 //把list这个集合中的所有元素都加到list2中*/

//    var map=Map("k"->"v","a"->"VVVVV","A"->"AAAAAAAA")
//    //println(map.get("k"))//通过get方法，根据key获取到对应的value
////    val value: String = map("kd")//根据key获取到value
////    println(value)
//
//    if(map.contains("k")){
//      println(map("k"))
//    }

    /*var map = mutable.Map("k"->"v")

    map("a")="vvv"//如果map中有a这个key，就把对应的value更为为vvv;否则，就添加一个k-v

    val value: Any = map.get("a").getOrElse(0)
    println(value)*/

    var set=Set(1,2,34,5,5,5,5,6)
    println(set.size)

    var set2=mutable.Set(5,6,7)
    set2+=1
    for(e <-set2){
      println(e)
    }

//    set2.

  }

}
