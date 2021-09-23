package com.baizhi.flink.broadcaststate

import org.apache.flink.api.common.state.{BroadcastState, MapStateDescriptor, ReadOnlyBroadcastState}
import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.scala.{BroadcastConnectedStream, DataStream, StreamExecutionEnvironment, createTypeInformation}
import org.apache.flink.util.Collector

/**
 * 敏感词可以是多个，还可以添加以及减少
 */
object HomeworkJob {
  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //获取两个数据流
    //高吞吐量流
    val pingLun: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    //低吞吐量流
    val minGanCi: DataStream[String] = environment.socketTextStream("hadoop10", 8888)
    val lowDataStream: DataStream[(String, String)] = minGanCi
      .map(_.split("\\s+"))
      .map(array => (array(0), array(1)))

    //把低吞吐量流转换成广播流
    //需要一个mapStateDescriptor；这个descriptor就是用来创建mapState的；这个mapState就是用来存储低吞吐量流中的数据
    //mapState里面key就是一个自己定义的字符串；value存储的就是低吞吐量流中的数据信息（一个字符串）==》mapState[String,String]
    var mapStateDescriptor:MapStateDescriptor[String,List[String]]=new MapStateDescriptor[String,List[String]]("msd",createTypeInformation[String],createTypeInformation[List[String]])
    val broadcastStream: BroadcastStream[(String, String)] = lowDataStream.broadcast(mapStateDescriptor)

    //高吞吐量流连接广播流形成广播连接流
    val broadcastConnectedStream: BroadcastConnectedStream[String, (String, String)] = pingLun.connect(broadcastStream)

    val result: DataStream[String] = broadcastConnectedStream.process(new MyBroadcastProcessFunction2(mapStateDescriptor))

    result.print()


    environment.execute("dataStreamBroadcastStateJob")
  }

}

class MyBroadcastProcessFunction2(mapStateDescriptor: MapStateDescriptor[String,List[String]]) extends BroadcastProcessFunction[String, (String, String),String]{
  override def processElement(value: String, ctx: BroadcastProcessFunction[String, (String, String), String]#ReadOnlyContext, out: Collector[String]): Unit = {

    val state: ReadOnlyBroadcastState[String, List[String]] = ctx.getBroadcastState(mapStateDescriptor)

    if(state.contains("minganci")){
      val list: List[String] = state.get("minganci")
//      println(list)

     /* var str:String=value
      for (elem <- list) {
        if(str.contains(elem)){
          str=str.replace(elem,"**")
        }
      }*/

      //通过算子完成操作处理
      val str1: String = list
        .fold(value)((v1, v2) => {
        if (v1.contains(v2)) {
          v1.replace(v2, "**")
        } else {
          v1
        }
      })

//      out.collect(str1+"**")
      out.collect(str1)
    }else{
//      out.collect(value+"&&&")
      out.collect(value)
    }
  }

  override def processBroadcastElement(value: (String, String), ctx: BroadcastProcessFunction[String, (String, String), String]#Context, out: Collector[String]): Unit = {

    val state: BroadcastState[String, List[String]] = ctx.getBroadcastState(mapStateDescriptor)
    //首先判断状态中有没有数据，如果有，把list集合获取到，如果添加，把value._1添加进去，剔除重复再放入到状态中;如果减，把value._1减去；如果没有数据，如果添加，就把value._1封装成list，放入到状态中

    if(state.contains("minganci")){
      val list: List[String] = state.get("minganci")
      if(value._2.equals("+")){
        //做加法
        val list1: List[String] = list :+ value._1
        val list2: List[String] = list1.distinct
        state.put("minganci",list2)
      }else{
        //做减法
//        list.dropWhile()//写上去试一试
        val list1: List[String] = list.filter(!_.equals(value._1))
        state.put("minganci",list1)
      }
    }else{
      if(value._2.equals("+")){
        //把数据封装起来，放入到状态中
        state.put("minganci",List(value._1))
      }
    }
  }
}
