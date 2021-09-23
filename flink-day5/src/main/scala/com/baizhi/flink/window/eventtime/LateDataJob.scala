package com.baizhi.flink.window.eventtime

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessAllWindowFunction
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
 * 迟到数据的处理
 */
object LateDataJob {

  def main(args: Array[String]): Unit = {

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //为了测试方便，把整体并行读设置为1
        environment.setParallelism(1)
//    environment.setParallelism(2)

    //设置flink程序使用事件时间
    environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)


    //使用固定周期的水位线提取方式，每间隔多长时间计算一次水位线
    //这个代码就是每间隔1秒提取一次水位线
    environment.getConfig.setAutoWatermarkInterval(1000)

    //过来的数据的格式是===》  数据  时间戳
    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val dataStream2: DataStream[(String, Long)] = dataStream
      .map(_.split("\\s+"))
      .map(array => (array(0), array(1).toLong))


    var outputTag:OutputTag[(String,Long)]=new OutputTag[(String, Long)]("wybj")
    val result: DataStream[String] = dataStream2
      .assignTimestampsAndWatermarks(new MyAssignerWithPeriodicWatermarks2)//指定一个水位线，这里设置的是固定周期的水位线
      .windowAll(TumblingEventTimeWindows.of(Time.seconds(5)))//基于事件时间的窗口
      .allowedLateness(Time.seconds(2))//允许迟到时间，也就是笔记中写的那个t。如果没有这一句话，迟到数据就直接丢弃掉（默认的做法）
      .sideOutputLateData(outputTag)//outputTag就是要给标记。这一句话的意思告诉flink计算引擎，把太迟的数据放入到outputTag这个标记里面
      .process(new MyProcessAllWindowFunction2)



    result.print("窗口中正常参与计算的数据")//小括号里面的参数就是一个前缀

    //从标记中获取数据
    val dataStream4: DataStream[(String, Long)] = result.getSideOutput(outputTag)
    dataStream4.print("too late")

    environment.execute("lateDataJob")
  }

}

class MyAssignerWithPeriodicWatermarks2 extends AssignerWithPeriodicWatermarks[(String, Long)]{

  /**
   * 生成水位线，每间隔固定时间生成一个水位线
   *
   * 水位线=最大事件时间-允许迟到时间
   * @return
   */

  //允许迟到时间：2秒
  private val allowLateTime:Long=2000

  //最大事件时间
  private var maxEventTime:Long=_

  override def getCurrentWatermark: Watermark = {
    //    println("*******************")
    new Watermark(maxEventTime-allowLateTime)
  }

  /**
   * 每过来一个元素，就提取到一个时间戳
   * @param element
   * @param previousElementTimestamp
   * @return
   */
  override def extractTimestamp(element: (String, Long), previousElementTimestamp: Long): Long = {

    //每过来一个元素，都计算一次最大事件时间
    maxEventTime=Math.max(maxEventTime,element._2)

    println(Thread.currentThread().getId+"==》水位线是："+(maxEventTime-allowLateTime))

    element._2
  }

}
//自定义的processAllWindowFunction
class MyProcessAllWindowFunction2 extends ProcessAllWindowFunction[(String,Long),String,TimeWindow]{
  override def process(context: Context, elements: Iterable[(String, Long)], out: Collector[String]): Unit = {


    val window: TimeWindow = context.window
    val start: Long = window.getStart
    val end: Long = window.getEnd
    println("窗口的开始以及结束时间是：["+start+","+end+")")

    //把窗口中的元素，用竖线连接起来返回出去
    val str: String = elements
      .map(_._1)
      /*.reduce((v1,v2)=>v1+"|"+v2)*///这一行代码和下面一行代码实现的效果是一样的
      .mkString("|")

    out.collect(str)

  }
}
