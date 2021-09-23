package com.baizhi.flink.first

import org.apache.flink.streaming.api.scala._

/**
 * 以word count 为例，看flink流计算的应用
 */
object FirstJob {

  def main(args: Array[String]): Unit = {
    //1.创建执行环境：本地运行以及远程服务器运行用统一的写法
    //可以获取到本地/远程服务器资源
    //2.加载数据源：socket
    //3.通过flink算子完成对应的操作处理：切分、映射、分组、统计
    //4.结果处理
    //5.执行程序

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //跨平台部署
    /*var jar="F:\\bigdata2102-code\\flink-day1\\target\\flink-day1-1.0-SNAPSHOT.jar"
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.createRemoteEnvironment("hadoop10",8081,jar)
*/
    //通过socketTextStream可以读取socket数据（nc -lk 9999）
    //这个dataStream里面装的一行一行的数据
    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    //数据的计算处理
    //这个dataStream2里面装的是一个一个的单词
    val dataStream2: DataStream[String] = dataStream.flatMap(_.split("\\s+"))
    //需要注意加入隐式转换 import org.apache.flink.streaming.api.scala._

    //这个dataStream3里面装的是一个一个的(单词,1)
    val dataStream3: DataStream[(String, Int)] = dataStream2.map((_, 1))

    //通过keyBy完成对单词的分组
    //通过keyBy算子计算完成之后，获取到的是keyedStream
    //keyedStream里面装的是一个一个的(单词,1)
    //keyedStream的后续操作，是基于key完成的操作处理
    //在[]里面的最后一个String表示的是key的类型（分组依据的类型）---单词的类型
    val keyedStream: KeyedStream[(String, Int), String] = dataStream3.keyBy(_._1)

    //对(单词,1)的第二个位置的数据（1）进行求和处理
    val result: DataStream[(String, Int)] = keyedStream.sum(1)

    result.print()//打印到控制台

    //执行程序
    environment.execute("first")

  }

}
