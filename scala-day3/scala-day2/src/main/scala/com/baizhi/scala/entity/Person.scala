package  com.baizhi.scala.entity

//通过class关键字声明一个类
class Person() {//无参数的主构造器，小括号可以省略

  //属性
  private var age:Int=18

  private var name:String=_//赋值一个默认值

  private var salary:Double=_

  println("*********")//其实就是主构造器中的代码

 def this(age:Int){
   this()//显示调用主构造器；直接调用主构造器
 }

  def this(age:Int,name:String){
    this(age)//显示调用辅助构造，由辅助构造器this(age)进一步调用主构造器。间接调用主构造器
  }
  //方法==函数
  def eat(food:String):Unit={
    println("吃："+food)
  }



}

