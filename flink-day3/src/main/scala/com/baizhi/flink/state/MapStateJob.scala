package com.baizhi.flink.state

import java.lang
import java.util.Date

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.state.{MapState, MapStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._

import scala.collection.JavaConverters._

/**
 * 需求：
 * 统计某APP内，用户访问的类别以及对应的访问次数
 * 通过状态完成数据的存储
 * 类别--->访问次数
 * MapState完成，key用类别，value表示访问的次数
 * 根据用户分组
 */
object MapStateJob {
  def main(args: Array[String]): Unit = {

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //接收到的数据的格式   用户id  访问的类别
    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    val keyedStream: KeyedStream[(Int, String), Int] = dataStream
      .map(_.split("\\s+"))
      .map(array => (array(0).toInt, array(1)))
      .keyBy(_._1)

    val result: DataStream[String] = keyedStream.map(new MyMapStateMapFunction)

    result.print()

    environment.execute("mapStateJob")

  }

}

class MyMapStateMapFunction extends RichMapFunction[(Int, String),String]{

  var mapState:MapState[String,Int]=_


  override def open(parameters: Configuration): Unit = {
    var mapStateDescriptor:MapStateDescriptor[String,Int]=new MapStateDescriptor[String,Int]("msd",createTypeInformation[String],createTypeInformation[Int])
    mapState=getRuntimeContext.getMapState(mapStateDescriptor)
  }

  /**
   * 通过mapState完成数据的处理
   *
   * @param value 输入的数据 ，元组 ，里面的第一个元素是用户id、第二个元素是用户访问的类别
   * @return 用户到目前为止访问过的类别以及对应的次数
   */
  override def map(value: (Int, String)): String = {

    //如果这一次访问的类别value._2在mapState里面，就获取到原来存储的访问次数，然后加1
    //如果这一次访问的类别value._2不在mapState里面，就把访问次数标记为1

    //通过mapState提供的方法完成开发处理
   /*if(mapState.contains(value._2)) {
     val count: Int = mapState.get(value._2)
     mapState.put(value._2,count+1)
   }else {
     mapState.put(value._2,1)
   }*/

    val category: String = value._2
    var count:Int=1
    if(mapState.contains(category)) {
      count+=mapState.get(category)
    }
    mapState.put(category,count)

    //构建返回值
    //从mapState里面，获取到所有的key以及对应的value


    /*val allKeys: lang.Iterable[String] = mapState.keys()

    val scalaIterable: Iterable[String] = allKeys.asScala
    val kvIterable: Iterable[String] = scalaIterable.map(key => key + "==>" + mapState.get(key))
    val str: String = kvIterable.mkString(",")*/

    /*val str: String = mapState.keys().asScala.map(key => key + "==>" + mapState.get(key)).mkString(",")
    value._1+"访问过的类别以及对应的次数："+str*/

    val str: String = mapState.entries().asScala.map(entry=>entry.getKey+"===>"+entry.getValue).mkString(",")
    value._1+"访问过的类别以及对应的次数："+str
  }
}
