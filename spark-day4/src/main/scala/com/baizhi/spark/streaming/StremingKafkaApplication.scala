package com.baizhi.spark.streaming

import org.apache.kafka.clients.consumer
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, ConsumerStrategy, KafkaUtils, LocationStrategies, LocationStrategy}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StremingKafkaApplication {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("streamingKafkaApp")
    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(5))

    //从kafka中读取数据
    //1.添加spark-streaming和kafka集成的依赖
    //2.写代码：按照对应的API完成开发
       //需要配置kafka主机、端口、topic、消费者组
      //需要配置反序列化机制
      //需要配置分区对应策略

    /**
     * 1.KafkaUtils就是sparkStreaming集成kafka之后提供的一个工具类
     * 2.createDirectStream就是里面的一个方法，用来创建dstream对象的
     *   用另外一句话讲，这个方法，就是从kafka中消费数据创建对应的dstream对象
     * 3.方法中需要三个参数
     *   a.ssc就是streamingContext对象
     *   b.locationStrategy:本地策略。大多数情况下，传递要给值[[LocationStrategies.PreferConsistent]]
     *     这里的参数描述的是kafka分区和rdd分区对应关系
     *   c.consumerStrategy:消费者策略。kafka相关的配置信息（主机、端口、topic、消费者组、key的反序列化机制、value的反序列化机制）
     */
    val locationStrategy:LocationStrategy=LocationStrategies.PreferConsistent


    val topics:List[String]=List("topica")
    //这个map中的配置，就应该配置主机、端口、反序列化机制、消费者组...
    val kafkaParams:Map[String,Object]=Map(
      //这个map中的key不是随便写的，而是根据ConsumerConfig类中定义的常量完成
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG->"hadoop10:9092",
      ConsumerConfig.GROUP_ID_CONFIG->"g1",
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG->"org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG->"org.apache.kafka.common.serialization.StringDeserializer"
    )
    val consumerStrategy:ConsumerStrategy[String,String]=ConsumerStrategies.Subscribe(topics,kafkaParams)

    var dstream:InputDStream[ConsumerRecord[String, String]]=KafkaUtils.createDirectStream(ssc,locationStrategy,consumerStrategy)


    //直接打印
    //获取到ConsumerRecord里面的value
    val dstream2: DStream[String] = dstream.map(consumerRecord => consumerRecord.value())
    dstream2.print()


    ssc.start()
    ssc.awaitTermination()
  }

}
