package com.baizhi.flink.datasource

import java.util.Properties

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema

object KafkaSourceJSONKeyValueDeserializationSchema {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    val props:Properties=new Properties()
    props.setProperty("bootstrap.servers","hadoop10:9092")
    var deserializer:JSONKeyValueDeserializationSchema=new JSONKeyValueDeserializationSchema(true)

    //特别注意的：kafka发送过来的key以及value都必须是json
    val kafkaSource: FlinkKafkaConsumer[ObjectNode] = new FlinkKafkaConsumer("topica", deserializer, props)
    val dataStream: DataStream[ObjectNode] = environment.addSource(kafkaSource)


//    dataStream.print()

    val result: DataStream[String] = dataStream.map(objectNode => objectNode.get("value").get("name").asText())

    result.print()
    environment.execute("kafkaSourceJSONKeyValueDeserializationSchemaJob")
  }

}
