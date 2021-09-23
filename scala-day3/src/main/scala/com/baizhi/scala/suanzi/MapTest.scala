package com.baizhi.scala.suanzi

object MapTest {

  def main(args: Array[String]): Unit = {
    //创建一个list集合

    //通过map算子，把list集合中的每一个元素加3

    val list:List[Int]=List(2,3,4,6,8)

    val newList: List[Int] = list.map((v: Int) => {
      v + 3
    })
    /*
    理解：
    1.(v:Int)表示匿名函数的参数列表
    2.=>就是在定义函数时的=
    3.匿名函数不需要写返回值类型，由函数体最后一行进行返回值类型的推导
    4.{}就是函数体，在里面写具体的代码，执行对应的功能
     */




    //基于上面的map算子里面的匿名函数，进行简化处理
    //1.{}可以省略
    val newList2: List[Int] = list.map((v: Int) => v + 3)
    //2.(),:,Int都可以省略
    val newList3: List[Int] = list.map(v => v + 3)//常用写法

    //3.如果变量在函数体中只使用一次，变量、=、>都可以省略，把使用变量位置的地方写_
    val newList4: List[Int] = list.map(_ + 3)//常用写法；_其实就表示list中的每一个元素
    for(e <-newList4){
      println(e)
    }
  }

}
