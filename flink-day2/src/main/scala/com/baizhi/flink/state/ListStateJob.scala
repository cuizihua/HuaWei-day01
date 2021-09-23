package com.baizhi.flink.state

import java.lang

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._

//scala中的集合实例和java中的集合实例相互转换需要的隐式转换
import scala.collection.JavaConverters._

/**
 * 通过状态统计用户访问过的类别
 */
object ListStateJob {
  def main(args: Array[String]): Unit = {

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //注意的是：接收到的数据格式是这样-->用户编号 访问的类别名
    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(Int, String), Int] = dataStream
      .map(_.split("\\s+"))
      .map(array => (array(0).toInt, array(1)))
      .keyBy(_._1)

    val result: DataStream[String] = keyedStream.map(new MyListStateMapFunction)

    result.print()


    environment.execute("listStateJob")

  }

}

class MyListStateMapFunction extends RichMapFunction[(Int, String),String]{

  //用使用listState存储的是用户访问的类别
  var listState:ListState[String]=_


  override def open(parameters: Configuration): Unit = {

    var listStateDescriptor:ListStateDescriptor[String]=new ListStateDescriptor[String]("listSD",createTypeInformation[String])
    listState = getRuntimeContext.getListState(listStateDescriptor)
  }

  override def map(value: (Int, String)): String = {

   /* //通过listState的方法完成对数据的处理
    listState.add(value._2)

    //
    val iterable: lang.Iterable[String] = listState.get()

    //把java中的对象，转换成scala中的对象:需要一个隐式转换import scala.collection.JavaConverters._
    val scalaIterable: Iterable[String] = iterable.asScala*/

   /* listState.add(value._2)

    val javaIterable: lang.Iterable[String] = listState.get()

    val scalaIterable: Iterable[String] = javaIterable.asScala

    val list: List[String] = scalaIterable.toList.distinct

    listState.update(list.asJava)//需要把list转换成java.util.list*/

    val list1: List[String] = listState.get().asScala.toList :+ value._2
    //先获取到状态中的数据
    //把现在的追加进去，重复剔除掉，然后更新状态
    val list: List[String] = list1.distinct

    listState.update(list.asJava)




    value._1+"访问过的类别有："+ list.mkString(",")
  }
}
