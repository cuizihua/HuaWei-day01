package com.baizhi.flink.datasource

import java.util.Properties

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, KafkaDeserializationSchema}
import org.apache.kafka.clients.consumer.ConsumerRecord

/**
 * 另外一个反序列化机制KafkaDeserializationSchema
 * 这个反序列化可以把元数据信息进行反序列化（topic partition offset ；key/value）
 */
object KafkaSourceKafkaDeserializationSchema {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment


    val topic:String="topica"
    val props:Properties=new Properties()
    props.setProperty("bootstrap.servers","hadoop10:9092")

    var deserializer:KafkaDeserializationSchema[(String,String, Int,Int)]=new MyKafkaDeserializationSchema
    var kafkaSource:FlinkKafkaConsumer[(String,String, Int,Int)]=new FlinkKafkaConsumer[(String,String, Int,Int)](topic,deserializer,props)
    val dataStream: DataStream[(String, String, Int, Int)] = environment.addSource(kafkaSource)

    dataStream.print()



    environment.execute("kafkaSourceKafkaDeserializationSchemaJob")
  }

}

/**
 * KafkaDeserializationSchema这个接口后面写的类型是自己随便写的，根据需要写的
 * 在这里的(String,String,Int,Int)表示反序列化之后的数据是一个元组，元组里面包含的内容依次是：key,value,partition,offset
 */
class MyKafkaDeserializationSchema extends KafkaDeserializationSchema[(String,String, Int,Int)]{
  override def isEndOfStream(nextElement: (String, String, Int, Int)): Boolean = false

  /**
   * 反序列化
   * @param record 一条记录：就是从kafka中读取的一条数据信息
   * @return 元组
   */
  override def deserialize(record: ConsumerRecord[Array[Byte], Array[Byte]]): (String, String, Int, Int) = {

    //从record对象中获取到需要的数据信息
    val byteArrayKey: Array[Byte] = record.key()
    val byteArrayValue: Array[Byte] = record.value()
    val partition: Int = record.partition()
    val longOffset: Long = record.offset()

    //对key进行非空判断，然后做字符串的解析
    var key:String=""
    if(byteArrayKey!=null){
      key=new String(byteArrayKey,"utf-8")
    }

    //封装数据
    (key,new String(byteArrayValue,"utf-8"),partition,longOffset.toInt)

  }

  /**
   * 反序列化之后的数据类型信息
   * @return
   */
  override def getProducedType: TypeInformation[(String, String, Int, Int)] = createTypeInformation[(String, String, Int, Int)]
}
