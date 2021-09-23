package com.baizhi.flink.state

import org.apache.flink.api.common.functions.{RichMapFunction, RuntimeContext}
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala._

/**
 * 以word count为例，看valueState的应用
 */
object ValueStateJob {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //通过socket加载数据源
    //发送过来的数据，是通过空格分隔开的数据
    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    //用空格截取，形成一个一个的单词，映射成元组，在根据元组的第一个元素（单词）分组
    val keyedStream: KeyedStream[(String, Int), String] = dataStream
      .flatMap(_.split("\\s+"))
      .map((_, 1))
      .keyBy(_._1)

    val dataStream2: DataStream[String] = keyedStream.map(new MyMapFunction)

    dataStream2.print()

    environment.execute("valueStateJob")
  }

}

/**
 * RichMapFunction后面有两个类型参数
 * 第一个是keyedStream的数据类型===》这个是不能变的
 * 第二个是自己随便写的，根据计算完成之后的结果选择对应的类型
 */
class MyMapFunction extends RichMapFunction[(String, Int),String]{
  //valueState里面存储的就是每一个单词到目前为止有多少个
  var valueState:ValueState[Int]=_



  //通过初始化方法创建valueState对象
  override def open(parameters: Configuration): Unit = {

    //在这里面创建状态；因为这个方法只执行一次
    //通过运行上下文完成状态对象的创建;runtimeContext对象里面提供的有方法，可以创建所有的状态
    val runtimeContext1: RuntimeContext = getRuntimeContext

    //状态描述者：描述状态数据信息的--》名字、类型、初始值
    //1.类型参数决定了由这个descriptor获取到的状态里面存储的数据的类型
    //2.构造方法里面的两个参数分别表示：名字：唯一标记和类型信息
    var valueStateDescriptor:ValueStateDescriptor[Int]=new ValueStateDescriptor[Int]("wordCountValueState",createTypeInformation[Int])

    //使用runtimeContext对象的getState方法就可以获取到valueState对象
    valueState=runtimeContext1.getState(valueStateDescriptor)

  }

  //每接收一个元素都会执行一次这个map方法
  //value就是接收到的元素---->(单词,1)
  //方法返回值就是到目前为止这个单词有多少个
  override def map(value: (String, Int)): String = {
    //通过valueState的一系列api完成对数据的计算统计处理

    //先从valueState中读取到这个key有多少个
    //然后加1
    //最后，把上面一步计算完成的结果放入到valueState里面

    val oldValue: Int = valueState.value()

    val newValue: Int = oldValue + value._2

    valueState.update(newValue)


    //构建方法值
    value._1+"--这个单词到目前为止有：-->"+valueState.value()
//    value._1+"--这个单词到目前为止有：-->"+newValue

  }
}
