package com.baizhi.flink.checkpoint

import com.baizhi.flink.ttl.MyTTLMapFunction
import org.apache.flink.api.common.functions.{ReduceFunction, RichMapFunction}
import org.apache.flink.api.common.state.{ReducingState, ReducingStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup
import org.apache.flink.streaming.api.scala._

object CheckpointJob {
  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    //配置checkpoint
    //5000:每间隔5000毫秒执行一次checkpoint。

    // CheckpointingMode.EXACTLY_ONCE:checkpointing模式是精准一次
    /**
     * This mode means that the system will
     * checkpoint the operator and user function state in such a way that, upon recovery,
     * every record will be reflected exactly once in the operator state.
     */

    //checkpointing模式还有一个值是CheckpointingMode.AT_LEAST_ONCE
    /**
     * Sets the checkpointing mode to "at least once". This mode means that the system will
     * checkpoint the operator and user function state in a simpler way. Upon failure and recovery,
     * some records may be reflected multiple times in the operator state.
     */

    environment.enableCheckpointing(5000,CheckpointingMode.EXACTLY_ONCE)

    //Sets the maximum time that a checkpoint may take before being discarded.
    // in milliseconds
    environment.getCheckpointConfig.setCheckpointTimeout(4000)

    //两次检查点间隔不得小于2秒，优先级高于checkpoint interval
    environment.getCheckpointConfig.setMinPauseBetweenCheckpoints(2000)

    //允许checkpoint失败的参数，默认值是0。取代了setFailOnCheckpointingErrors(boolean)
    environment.getCheckpointConfig.setTolerableCheckpointFailureNumber(2)

    //当任务取消时，检查点数据该如何处理
    //RETAIN_ON_CANCELLATION:任务取消时，没有加savepoint,检查点数据保留
    //DELETE_ON_CANCELLATION：任务取消时，检查点数据删除（不建议使用）
    environment.getCheckpointConfig.enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)

    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)


    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    val result: DataStream[String] = keyedStream.map(new MyCheckPointMapFunction)
    result.print()

    environment.execute("chekpointJob")
  }

}

class MyCheckPointMapFunction extends RichMapFunction[(String, Int), String]{
  var reducingState:ReducingState[Int]=_



  override def open(parameters: Configuration): Unit = {

    val reduceFunction: ReduceFunction[Int] = new ReduceFunction[Int] {
      override def reduce(value1: Int, value2: Int): Int = {
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
