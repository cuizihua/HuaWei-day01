package com.baizhi.flink.datasource

import java.util.Properties

import org.apache.flink.api.common.serialization.{DeserializationSchema, SimpleStringSchema}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object KafkaSource {

  def main(args: Array[String]): Unit = {
    /**
     * 执行环境
     * 加载数据源
     * 数据的处理
     * sink
     * 执行job
     */

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val topicName:String="topica"
    //最简单的字符串反序列化机制:把字节数组转换成字符串，以UTF-8的编码方式转换
    var valueDeserializer:DeserializationSchema[String]=new SimpleStringSchema()
    val props:Properties=new Properties()
    //"bootstrap.servers"这个key就是kafka的主机以及端口
    props.setProperty("bootstrap.servers","hadoop10:9092")

    val kafkaSource: FlinkKafkaConsumer[String] = new FlinkKafkaConsumer[String](topicName, valueDeserializer, props)
    val dataStream: DataStream[String] = environment.addSource(kafkaSource)

    dataStream.print()

    environment.execute("kafkaSourceJob")
  }

}
