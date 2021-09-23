package com.baizhi.flink.queryablestate

import org.apache.flink.api.common.functions.{ReduceFunction, RichMapFunction}
import org.apache.flink.api.common.state.{ReducingState, ReducingStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._

/**
 * 以wordcount 为例看一下状态可查询的具体代码实现以及部署之后的效果
 */
object QueryableStateJob {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    val result: DataStream[String] = keyedStream.map(new MyQueryableStateMapFunction)
    result.print()

    environment.execute("queryableStateJob")
  }

}

class MyQueryableStateMapFunction extends RichMapFunction[(String, Int), String]{

  var reducingState:ReducingState[Int]=_


  override def open(parameters: Configuration): Unit = {

    val rsd: ReducingStateDescriptor[Int] = new ReducingStateDescriptor[Int]("rsd", new ReduceFunction[Int] {
      override def reduce(value1: Int, value2: Int): Int = value1 + value2
    }, createTypeInformation[Int])

    //通过descriptor的setQueryable方法设置状态可查询
    rsd.setQueryable("word-count-queryable-state")

    reducingState=getRuntimeContext.getReducingState(rsd)
  }

  override def map(value: (String, Int)): String = {

    reducingState.add(value._2)

    //构建返回值
    value._1+"的个数是："+reducingState.get()
  }
}
