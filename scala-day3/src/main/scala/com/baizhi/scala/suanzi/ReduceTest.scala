package com.baizhi.scala.suanzi

object ReduceTest {

  def main(args: Array[String]): Unit = {
//    var list=List(1,2,3,4,5,6)

    //理解：
    //1.reduce是一个聚合函数（把所有的元素都聚合在一起）
    //2.把第一个元素获取到赋值给第一个形参；
    // 3.每一次循环获取到集合中剩余的元素赋值给v2
    //每一次循环执行的函数体赋值给v1
    //reduce算子中的形参函数执行多少次？list.size-1
    //v1就表示上一次计算完成之后的结果
    //reduce算子要求参数类型和计算结果类型必须一致
    /*val sum: Int = list.reduce((v1, v2) => {
      println(v1+"****"+v2)
      v1 + v2//加法
    })

    println("sum="+sum)*/


    //需求：把list中的元素，用※连接起来，最后获取到的是字符串
    var list=List(1,2,3,4,5,6)
    val strs: List[String] = list.map(_ + "")
    val str: String = strs.reduce((v1, v2) => v1 + "※" + v2)
    println(str)
  }

}
