package com.baizhi.scala
import scala.collection.immutable
import scala.util.control.Breaks

object LoopDemo {

  def main(args: Array[String]): Unit = {

    /*for (i<-1 to 10){
      println(i)
    }*/
    /*for (i<-1 until  10){
      println(i)
    }*/

    //把1-10（包括）所有的偶数显示出来

   /* for (i<-1 to 10){
      if(i%2==0){
        println(i)
      }
    }*/
    /*for(i<-1 to 10 if i%2==0){
      println(i)
    }*/

    /*val ints = for (i <- 1 to 10) yield i
    print(ints)*/

    //嵌套循环
    //显示九九乘法口诀表
    /*for(i <- 1 to 9){//i表示行数
      for(j<-1 to i){//j表示的列数
        print(i+"*"+j+"="+i*j+" ")
      }
      println()
    }*/

    //更简单的写法
    /*for(i<-1 to 9;j<-1 to i ){
      print(i+"*"+j+"="+i*j+" ")
      if(i==j){
        println()
      }
    }*/

    //1到10循环，如果遇到5就直接结束掉
   /* Breaks.breakable(
      for (i<-1 to 10){
        if(i==5){
          //这里应该结束循环
          Breaks.break()
        }
        println(i)
      }
    )*/
    Breaks.breakable {
      for (i<-1 to 10){
        if(i==5){
          //这里应该结束循环
          Breaks.break()
        }
        println(i)
      }
    }



  }

}
