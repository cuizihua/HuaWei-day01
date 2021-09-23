package com.baizhi.spark.streaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

/**
 * 以word count为例完成流计算
 * 1.通过socket读取hadoop10机器中的9999端口发送的数据
 * 2.通过sparkStreaming完成流计算
 * 3.把计算结果在控制台直接显示
 */
object FirstSparkStreamingApplication {

  def main(args: Array[String]): Unit = {
    /**
     * 1.创建sparkConf：在这里面设置master以及appName
     * 2.创建streamingContext==>ssc,需要使用sparkConf，同时需要设置批次
     * 3.读取数据源
     * 4.通过算子完成数据的处理
     * 5.计算结果的处理：print/保存起来
     * 6.让程序启动并且一直处于运行状态
     */

    val sparkConf: SparkConf = new SparkConf().setAppName("firstSSApp").setMaster("local[*]")

//    val duration:Duration=Duration(2000)//2秒；伴生对象的使用
    val duration:Duration=Seconds(2)//2秒；伴生对象的使用
    val ssc: StreamingContext = new StreamingContext(sparkConf, duration)

    //StreamingContext对象
    //1.需要配置以及持续时间（微批批次）
    //2.里面包含了sparkContext对象==》在流计算中，
    // 如果需要sparkContext，不需要重新创建，而是可以通过streamingContext对象获取到
    // val sc: SparkContext = ssc.sparkContext

    //通过socket，到hadoop10这台机器中的9999端口上读取数据
    //读取到的就是一个dstream对象
    //接收的数据格式   单词  单词....
    val dstream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop10", 9999)

    //重点要聊的就是sparkStreaming到kafka中读取数据


    //通过dstream提供的算子完成数据的处理
    //dstream的算子和rdd算子一样

    val dstream2: DStream[String] = dstream.flatMap(_.split("\\s+"))
    val dstream3: DStream[(String, Int)] = dstream2.map((_, 1))
    val dstream4: DStream[(String, Int)] = dstream3.reduceByKey((v1, v2) => v1 + v2)

    //每一个批次都是单独计算的。这样的计算被称为无状态计算。spark流计算的默认状态
    //同时spark还支持有状态计算：

    //流计算中还有一个比较重要的概念：窗口计算



    //sink:最后的输出
    dstream4.print()


    //6
    ssc.start()//启动
    ssc.awaitTermination()//持续运行

  }

}
