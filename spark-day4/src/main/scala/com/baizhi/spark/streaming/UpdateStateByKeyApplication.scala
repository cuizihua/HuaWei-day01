package com.baizhi.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 通过updateStateByKey完成sparkStreaming的有状态计算
 * 到目前为止，获取到的是所有批次的数据信息
 *
 * 以word count为例完成代码
 */
object UpdateStateByKeyApplication {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("updateSBKApp")
    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(5))


    ssc.checkpoint("hdfs://hadoop10:9000/spark-streaming-checkpoint")

    val dstream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop10", 9999)

    val dstream2: DStream[(String, Int)] = dstream
      .flatMap(_.split("\\s+"))
      .map((_, 1))

    //(seq,option)=>option,前面这些代码，表示是updateStateByKey这个算子需要的函数
    //上面的这个代码对应的函数表示需要两个参数，返回值一个Option对象
    //两个参数分别表示：
    // seq:当前批次key对应的数据形成的集合。在我们的这个代码背景下，seq里面就存储了多个1
    // option：上一个批次计算完成之后，key对应的个数封装形成的Option对象
    //函数返回值：就表示到当前批次为止，key对应的个数封装形成的Option对象。
    // 函数返回值作为下一个批次计算的依据
//    dstream2.updateStateByKey((seq,option)=>option)
    val result: DStream[(String, Int)] = dstream2
      .updateStateByKey((seq: Seq[Int], option: Option[Int]) => {
      //1.获取到当前批次key对应的个数：seq里面的所有数据做聚合处理
      //2.获取到上一个批次key对应的个数

      //3.把当前批次key的个数和上一个批次key的个数做加法运算


//      val currentValue: Int = seq.reduce(_ + _)
//      val currentValue: Int = seq.reduce((v1,v2)=>{
//        println(v1+"((****))"+v2)
//        v1+v2
//      })
//        println(seq.mkString(",")+"******))(((")
//      val currentValue: Int =1
      val currentValue: Int = seq.fold(0)(_ + _)

      val preValue: Int = option.getOrElse(0)
      Some(currentValue + preValue)
    })

    result.print()

    ssc.start()
    ssc.awaitTermination()
  }

}




