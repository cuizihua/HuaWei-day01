package com.baizhi.flink.window.function

import java.text.SimpleDateFormat

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.common.state.{KeyedStateStore, ReducingState, ReducingStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object ReduceAndProcessJob {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    val windowedStream: WindowedStream[(String, Int), String, TimeWindow] = keyedStream.window(TumblingProcessingTimeWindows.of(Time.seconds(10)))

    //
    val result: DataStream[String] = windowedStream.reduce(new MyReduceFunction2, new MyProcessWindowFunction2)
    result.print()


    environment.execute("reduceAndProcessJob")
  }
}

class MyReduceFunction2 extends ReduceFunction[(String, Int)]{
  override def reduce(value1: (String, Int), value2: (String, Int)): (String, Int) = {
    println(value2+"reduceFunction")
    (value1._1,value1._2+value2._2)
  }
}

class MyProcessWindowFunction2 extends ProcessWindowFunction[(String, Int),String,String,TimeWindow]{


  /**
   * 所有落入到窗口的元素，都保存起来形成了一个集合，在触发窗口计算函数的时候，执行一次这个方法
   *
   * @param key key
   * @param context 上下文
   * @param elements 元素，落入到窗口的所有元素，集合
   * @param out 输出
   */
  override def process(key: String, context: Context, elements: Iterable[(String, Int)], out: Collector[String]): Unit = {

        println(elements.mkString(",")+"process")//

    //窗口元数据信息
    val timeWindow: TimeWindow = context.window
    val start: Long = timeWindow.getStart//窗口的开始时间
    val end: Long = timeWindow.getEnd//窗口结束时间

    val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val startStr: String = format.format(start)
    val endStr: String = format.format(end)

    println("窗口的时间范围是：["+startStr+","+endStr+")")//

    val count: Int = elements.map(_._2).sum

    out.collect(key+"个数是："+count)

  }
}
