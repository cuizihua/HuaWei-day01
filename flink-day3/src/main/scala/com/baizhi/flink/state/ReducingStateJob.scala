package com.baizhi.flink.state

import org.apache.flink.api.common.functions.{ReduceFunction, RichMapFunction}
import org.apache.flink.api.common.state.{ReducingState, ReducingStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._

/**
 * 通过reducingState完成word count功能
 * reducingState:存储一个值，可以自动运算
 * 根据word进行keyby，reducingState里面存储的就是单词的个数
 */
object ReducingStateJob {
  def main(args: Array[String]): Unit = {

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    val result: DataStream[String] = keyedStream.map(new MyReducingStateMapFunction)

    result.print()


    environment.execute("reducingStateJob")
  }

}

class  MyReducingStateMapFunction extends RichMapFunction[(String, Int),String]{

  var reducingState:ReducingState[Int]=_



  override def open(parameters: Configuration): Unit = {

    val reduceFunction: ReduceFunction[Int] = new ReduceFunction[Int] {
      override def reduce(value1: Int, value2: Int): Int = {
        println(value1+"**"+value2)
        value1 + value2
      }
    }
    var reducingStateDescriptor:ReducingStateDescriptor[Int]=new ReducingStateDescriptor[Int]("rsd",reduceFunction,createTypeInformation[Int])
    reducingState=getRuntimeContext.getReducingState(reducingStateDescriptor)
  }

  override def map(value: (String, Int)): String = {

    //把单词个数添加到reducingState里面，有reducingState的自动计算函数完成计算处理
    reducingState.add(value._2)

    //构建返回值
    value._1+"单词的个数是："+reducingState.get()
  }
}
