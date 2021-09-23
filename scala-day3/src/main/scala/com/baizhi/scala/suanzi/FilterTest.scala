package com.baizhi.scala.suanzi

object FilterTest {

  def main(args: Array[String]): Unit = {
    //filter算子:做过滤操作，它需要的函数参数返回值是Boolean类型
    //filter算子，会把集合中的每一个元素获取到，应用函数参数进行计算处理，
    // 如果这个元素计算完成之后是true，就把这个元素留下
    var list=List(1,2,3,4)

    //把能被3整除的数据留下
    val newList: List[Int] = list.filter(_ % 3 == 0)


    //把集合中能被3整除的数据排除掉
    val newList3: List[Int] = list.filter(_ % 3 != 0)
    for(e<-newList3){
      println(e)
    }
  }

}
