package com.baizhi.flink.window.function

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.common.state.{KeyedStateStore, ReducingState, ReducingStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object ProcessWindowFunctionJob {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    val windowedStream: WindowedStream[(String, Int), String, TimeWindow] = keyedStream.window(TumblingProcessingTimeWindows.of(Time.seconds(10)))

    val result: DataStream[String] = windowedStream.process(new MyProcessWindowFunction)
    result.print()

    environment.execute("processWindowFunction")
  }

}

/**
 * 四个类型参数分别是IN, OUT, KEY, Window(窗口类型：TimeWindow/GlobalWindow)
 */
class MyProcessWindowFunction extends ProcessWindowFunction[(String, Int),String,String,TimeWindow]{

  var rsd:ReducingStateDescriptor[Int]=_
  override def open(parameters: Configuration): Unit = {

    rsd=new ReducingStateDescriptor[Int]("rsd",new ReduceFunction[Int] {
      override def reduce(value1: Int, value2: Int): Int = value1+value2
    },createTypeInformation[Int])
  }

  /**
   * 所有落入到窗口的元素，都保存起来形成了一个集合，在触发窗口计算函数的时候，执行一次这个方法
   *
   * @param key key
   * @param context 上下文
   * @param elements 元素，落入到窗口的所有元素，集合
   * @param out 输出
   */
  override def process(key: String, context: Context, elements: Iterable[(String, Int)], out: Collector[String]): Unit = {

//    println(elements.mkString(","))//(a,1),(a,1)

    //窗口元数据信息
    val timeWindow: TimeWindow = context.window
    val start: Long = timeWindow.getStart//窗口的开始时间
    val end: Long = timeWindow.getEnd//窗口结束时间

    //可以获取到状态
    val windowState: KeyedStateStore = context.windowState//基本没用

    //全局状态：可以把所有窗口的数据做统计汇总处理；让所有窗口处于状态中
    val globalState: KeyedStateStore = context.globalState
    val reducingState: ReducingState[Int] = globalState.getReducingState(rsd)

    val count: Int = elements.map(_._2).sum

    reducingState.add(count)

    out.collect(key+"个数是："+count+";到目前为止，所有窗口"+key+"这个单词的个数是："+reducingState.get())

  }
}
