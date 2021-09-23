package com.baizhi.flink.broadcaststate

import org.apache.flink.api.common.state.{BroadcastState, MapStateDescriptor, ReadOnlyBroadcastState}
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector

/**
 * 以评论为例完成dataStream连接广播流的广播状态的使用
 * 1.评论是高吞吐量流
 * 2.敏感词是低吞吐量流
 */
object DataStreamBroadcastStateJob {
  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //获取两个数据流
    //高吞吐量流
    val pingLun: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    //低吞吐量流
    val minGanCi: DataStream[String] = environment.socketTextStream("hadoop10", 8888)

    //把低吞吐量流转换成广播流
    //需要一个mapStateDescriptor；这个descriptor就是用来创建mapState的；这个mapState就是用来存储低吞吐量流中的数据
    //mapState里面key就是一个自己定义的字符串；value存储的就是低吞吐量流中的数据信息（一个字符串）==》mapState[String,String]
    var mapStateDescriptor:MapStateDescriptor[String,String]=new MapStateDescriptor[String,String]("msd",createTypeInformation[String],createTypeInformation[String])
    val broadcastStream: BroadcastStream[String] = minGanCi.broadcast(mapStateDescriptor)

    //高吞吐量流连接广播流形成广播连接流
    val broadcastConnectedStream: BroadcastConnectedStream[String, String] = pingLun.connect(broadcastStream)

    val result: DataStream[String] = broadcastConnectedStream.process(new MyBroadcastProcessFunction(mapStateDescriptor))

    result.print()


    environment.execute("dataStreamBroadcastStateJob")
  }

}

///根据高吞吐量流是否是keyedStream进行选择需要继承的processFunction
//如果高吞吐量流是keyedStream，就继承KeyedBroadcastProcessFunction
//如果高吞吐量流式dataStream，就继承BroadcastProcessFunction
class MyBroadcastProcessFunction(mapStateDescriptor: MapStateDescriptor[String,String]) extends BroadcastProcessFunction[String,String,String]{

  /**
   * 用来处理高吞吐量流中的数据
   *
   * @param value 高吞吐量流中的数据
   * @param ctx 只读上下文,可以以只读的方式获取到state对象
   * @param out 输出，通过out把需要往下游走的数据返回出去
   *
   *            在这个方法里面，以只读的方式从状态中获取数据
   */
  override def processElement(value: String, ctx: BroadcastProcessFunction[String, String, String]#ReadOnlyContext, out: Collector[String]): Unit = {
    //1.获取到状态
    val readOnlyBroadcastState: ReadOnlyBroadcastState[String, String] = ctx.getBroadcastState(mapStateDescriptor)

    if(readOnlyBroadcastState.contains("minganci")){
      val minGanCi: String = readOnlyBroadcastState.get("minganci")

      //把高吞吐量流中的数据中的包含的敏感词用**替换掉
      if(value.contains(minGanCi)){
       val str:String= value.replace(minGanCi,"**")
        out.collect(str)
      }else{
        out.collect(value+"&&")
      }

    }else{
      //还没有敏感词，就把高吞吐量流中的原始数据发送出去
      out.collect(value+"^^")
    }
  }



  /**
   * 用来处理低吞吐量流中的数据
   *
   * @param value 低吞吐量流中的数据
   * @param ctx 上下文,可以获取到state对象
   * @param out 输出，通过out把需要往下游走的数据返回出去
   *
   *            在这个方法里面，把数据放入到状态中
   */
  override def processBroadcastElement(value: String, ctx: BroadcastProcessFunction[String, String, String]#Context, out: Collector[String]): Unit = {

    //通过状态描述者，通过上下文对象，获取到一个状态对象
    val broadcastState: BroadcastState[String, String] = ctx.getBroadcastState(mapStateDescriptor)

    //把低吞吐量流中的数据放入到状态中
    broadcastState.put("minganci",value)

  }

  //两个方法使用的是同一个状态对象，才能保证，在高吞吐量流里面获取到低吞吐量流中的数据
  //用同一个状态描述者，获取到的就是同一个状态对象
}
