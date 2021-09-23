package com.baizhi.ure.job

import java.{lang, util}
import java.util.{ArrayList, List, Properties}

import com.baizhi.em.entity.{EvalauteData, EvaluateReport, HistoryData, LoginSuccessData}
import com.baizhi.em.evaluate.{CityEvaluate, Evaluate, EvaluateChain, InputFeatureEvaluate, SimilirtyEvaluate, SpeedEvaluate, TimeslotEvaluate, TotalCountEvaluate}
import com.baizhi.em.update.{CityUpdate, DeviceUpdate, InputFeaturesUpdate, PasswordUpdate, TimeSlotUpdate, Update, UpdateChain}
import com.baizhi.em.util.Util
import org.apache.flink.api.common.functions.{ReduceFunction, RichMapFunction}
import org.apache.flink.api.common.serialization.{DeserializationSchema, Encoder, SimpleStringEncoder, SimpleStringSchema}
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, MapState, MapStateDescriptor, ReducingState, ReducingStateDescriptor, StateTtlConfig, ValueState, ValueStateDescriptor}
import org.apache.flink.api.common.time.Time
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.fs.Path
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.{Collector, Preconditions}

import scala.collection.JavaConverters._
import scala.collection.mutable


object UserRiskEvaluateJob {

  def main(args: Array[String]): Unit = {

    //按照开发步骤完成
    //先不到kakfa中取数据

    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //设置checkpoint


    //模拟数据源socket
    val dataStream: DataStream[String] = environment.socketTextStream("hadoop10", 9999)

    //从kafka中消费数据
    //kafka的配置
    /*var properties:Properties=new Properties()
    properties.setProperty("bootstrap.servers","hadoop10:9092")

    //反序列化机制
    var deserializationSchema:DeserializationSchema[String]=new SimpleStringSchema()//字符串反序列化机制
    var flinkKafkaConsumer:FlinkKafkaConsumer[String]=new FlinkKafkaConsumer[String]("topica",deserializationSchema,properties)

    //从kafka消费数据
    val dataStream: DataStream[String] = environment.addSource(flinkKafkaConsumer)*/


    //完成对应的功能处理
    //通过状态完成数据的存储
    //自定义mapFunction完成
    //思考：
    // 1.是评估或者是登录成功的数据，再执行计算处理==》filter
    // 2.keyBy--->根据什么做？应用名+用户名

    val result: DataStream[EvaluateReport] = dataStream.filter(log => Util.isEvaluateOrSuccess(log))
      .map(log => (Util.getAppNameAndUsername(log), log))
      .keyBy(_._1) //应用名和用户名实现keyBy
      //.map(new MyMapFunction)//通过自定义mapFunction完成业务功能的处理；但是状态可查询不支持TTL
      .process(new MyKeyedProcessFunction)
      .filter(_ != null)//把评估报告留下

//    result.print()//写入到hdfs
    //1.flink对文件系统有支持，可以直接把数据写入到文件系统

    //构建一个sink对象
    var path:Path=new Path("hdfs://hadoop10:9000/final-project")
    var encoder:Encoder[EvaluateReport]=new SimpleStringEncoder()
    val fileSink: StreamingFileSink[EvaluateReport] = StreamingFileSink
      .forRowFormat(path, encoder)
      .withBucketAssigner(new DateTimeBucketAssigner[EvaluateReport]("yyyy-MM"))//根据我给定的格式生成文件夹
      .build()

    result.addSink(fileSink)


    environment.execute("eserRiskEvaluateJob")

  }

}

class MyKeyedProcessFunction extends KeyedProcessFunction[String,(String,String),EvaluateReport]{

  var listState:ListState[String]=_

  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, (String, String), EvaluateReport]#OnTimerContext, out: Collector[EvaluateReport]): Unit = {

//    print("&&&&")


    //获取到listState里面的第一个元素
    val iterable: lang.Iterable[String] = listState.get()

    val scalaIterable: Iterable[String] = iterable.asScala
    val list: scala.List[String] = scalaIterable.toList

    val javaList: util.List[String] = list.asJava
    val uuid: String = javaList.get(0)//取listState中的第一个元素


    //从评估报告状态中，把要过去的那一个移除掉
    evaluateReportState.remove(uuid)

    //同时把listState中的数据更新一下
    //把ScalaList转换成buffer，从里面把第一个移除掉
    val buffer: mutable.Buffer[String] = list.toBuffer
    buffer.remove(0)
    val list1: scala.List[String] = buffer.toList

    listState.update(list1.asJava);


  }

  var evaluateReportState:MapState[String,String]=_

  var historyDataState:ValueState[HistoryData]=_

  var currentDayTotalLoginCountState:ReducingState[Int]=_



  override def open(parameters: Configuration): Unit = {

    //创建状态描述者，创建对应的状态

    listState=getRuntimeContext.getListState(new ListStateDescriptor[String]("ls",createTypeInformation[String]))

    val mapStateDescriptor: MapStateDescriptor[String, String] = new MapStateDescriptor[String, String]("msd",createTypeInformation[String],createTypeInformation[String])
//    mapStateDescriptor.setQueryable("evaluateReportState")

    /*val ttlConfig: StateTtlConfig = StateTtlConfig.newBuilder(Time.minutes(1)).build()
    mapStateDescriptor.enableTimeToLive(ttlConfig)*/

    evaluateReportState=getRuntimeContext.getMapState(mapStateDescriptor)

    val valueStateDescriptor: ValueStateDescriptor[HistoryData] = new ValueStateDescriptor[HistoryData]("vsd",createTypeInformation[HistoryData])
    historyDataState=getRuntimeContext.getState(valueStateDescriptor)

    val reduceFunction: ReduceFunction[Int] = new ReduceFunction[Int] {
      override def reduce(value1: Int, value2: Int): Int = value1+value2
    }
    val reducingStateDescriptor: ReducingStateDescriptor[Int] = new ReducingStateDescriptor[Int]("rsd",reduceFunction,createTypeInformation[Int])
    currentDayTotalLoginCountState=getRuntimeContext.getReducingState(reducingStateDescriptor)
  }

  /*override def processElement(value: (String, String), ctx: KeyedProcessFunction[String, (String, String), EvaluateReport]#Context, out: Collector[EvaluateReport]): Unit = {
    println("**"+value+"***")
    val timer: Long = ctx.timerService().currentProcessingTime() +60000//60秒钟之后，调用onTimer
    ctx.timerService.registerProcessingTimeTimer(timer)

  }*/


  override def processElement(value: (String, String), ctx: KeyedProcessFunction[String, (String, String), EvaluateReport]#Context, out: Collector[EvaluateReport]): Unit = {
    /*println("***"+value+"****")
    val timer: Long = ctx.timerService().currentProcessingTime() +10000//10秒钟之后，调用onTimer
    ctx.timerService.registerProcessingTimeTimer(timer)*/
    val log: String = value._2

    //    println("******"+log)
    if(Util.isEvaluate(log)){
      //评估
      val evalauteData: EvalauteData = Util.parseLog2EvaluateData(log)
      val evaluateReport: EvaluateReport = new EvaluateReport(evalauteData.getAppName, evalauteData.getUsername, evalauteData.getUuid, evalauteData.getTime, evalauteData.getCity, evalauteData.getGeoPoint)

      val evaluate1: Evaluate = new CityEvaluate
      val evaluate2: Evaluate = new InputFeatureEvaluate
      val evaluate3: Evaluate = new SimilirtyEvaluate(0.95)
      val evaluate4: Evaluate = new SpeedEvaluate(750)
      val evaluate5: Evaluate = new TimeslotEvaluate(2)
      val evaluate6: Evaluate = new TotalCountEvaluate(10)
      //        Evaluate evaluate7=new CityEvaluate();

      val list: List[Evaluate] = new ArrayList[Evaluate]
      list.add(evaluate1)
      list.add(evaluate2)
      list.add(evaluate3)
      list.add(evaluate4)
      list.add(evaluate5)
      //        list.add(evaluate6);

      val chain: EvaluateChain = new EvaluateChain(list)

      var historyData: HistoryData = historyDataState.value()
      if(historyData==null){
        historyData=new HistoryData()
      }
      chain.doEvaluate(historyData, evalauteData, evaluateReport)


      //      println(evaluateReport+"****")
      //把评估报告放入到状态中
      //把evaluateReport转换成json
      val str: String = new ObjectMapper().writeValueAsString(evaluateReport)
      //      println(str+"str")//1个问题

      evaluateReportState.put(evalauteData.getUuid,str)

      listState.add(evalauteData.getUuid)
      val timer: Long = ctx.timerService().currentProcessingTime() +5000//5秒钟之后，调用onTimer
      ctx.timerService.registerProcessingTimeTimer(timer)

      currentDayTotalLoginCountState.add(1)//每一次登录都被标记1次；无论登录成功与否，都算登录1次
//      return evaluateReport
      out.collect(evaluateReport);
    }else{
      val loginSuccessData: LoginSuccessData = Util.parseLog2LoginSuccessData(log)

      //走更新链把登录成功的数据存入到历史数据中
      val update1: Update = new DeviceUpdate(10)
      val update2: Update = new CityUpdate
      val update3: Update = new InputFeaturesUpdate(10)
      val update4: Update = new PasswordUpdate
      val update5: Update = new TimeSlotUpdate

      val list: util.List[Update] = new util.ArrayList[Update]
      list.add(update1)
      list.add(update2)
      list.add(update3)
      list.add(update4)
      list.add(update5)

      val chain: UpdateChain = new UpdateChain(list)
      var historyData: HistoryData = historyDataState.value()
      if(historyData==null){
        historyData=new HistoryData()
      }

      chain.doUpdate(loginSuccessData, historyData)

      historyData.setLastLoginTime(loginSuccessData.getTime)
      historyData.setLastLoginGeoPoint(loginSuccessData.getGeoPoint)

      historyData.setCurrentDayLoginCount(currentDayTotalLoginCountState.get())

      historyDataState.update(historyData)
    }


  }
}

class MyMapFunction extends RichMapFunction[(String,String),EvaluateReport]{

  //评估报告状态---》类型MapState[uuid,EvaluateReport]
  //历史数据状态---》类型ValueState

  //当天累计登录次数--》类型ReducingState


  var evaluateReportState:MapState[String,String]=_

  var historyDataState:ValueState[HistoryData]=_

  var currentDayTotalLoginCountState:ReducingState[Int]=_



  override def open(parameters: Configuration): Unit = {

    //创建状态描述者，创建对应的状态

    val mapStateDescriptor: MapStateDescriptor[String, String] = new MapStateDescriptor[String, String]("msd",createTypeInformation[String],createTypeInformation[String])
    mapStateDescriptor.setQueryable("evaluateReportState")

    val ttlConfig: StateTtlConfig = StateTtlConfig.newBuilder(Time.minutes(1)).build()
    mapStateDescriptor.enableTimeToLive(ttlConfig)

    evaluateReportState=getRuntimeContext.getMapState(mapStateDescriptor)

    val valueStateDescriptor: ValueStateDescriptor[HistoryData] = new ValueStateDescriptor[HistoryData]("vsd",createTypeInformation[HistoryData])
    historyDataState=getRuntimeContext.getState(valueStateDescriptor)

    val reduceFunction: ReduceFunction[Int] = new ReduceFunction[Int] {
      override def reduce(value1: Int, value2: Int): Int = value1+value2
    }
    val reducingStateDescriptor: ReducingStateDescriptor[Int] = new ReducingStateDescriptor[Int]("rsd",reduceFunction,createTypeInformation[Int])
    currentDayTotalLoginCountState=getRuntimeContext.getReducingState(reducingStateDescriptor)
  }

  //如果是评估，就返回评估报告对象；如果是登录成功就返回null
  override def map(value: (String, String)): EvaluateReport = {

    //如果是评估数据，就走评估链生成评估报告
    //如果是登录成功数据，就走更新链，把历史数据更新

    val log: String = value._2

//    println("******"+log)
    if(Util.isEvaluate(log)){
      //评估
      val evalauteData: EvalauteData = Util.parseLog2EvaluateData(log)
      val evaluateReport: EvaluateReport = new EvaluateReport(evalauteData.getAppName, evalauteData.getUsername, evalauteData.getUuid, evalauteData.getTime, evalauteData.getCity, evalauteData.getGeoPoint)

      val evaluate1: Evaluate = new CityEvaluate
      val evaluate2: Evaluate = new InputFeatureEvaluate
      val evaluate3: Evaluate = new SimilirtyEvaluate(0.95)
      val evaluate4: Evaluate = new SpeedEvaluate(750)
      val evaluate5: Evaluate = new TimeslotEvaluate(2)
      val evaluate6: Evaluate = new TotalCountEvaluate(10)
      //        Evaluate evaluate7=new CityEvaluate();

      val list: List[Evaluate] = new ArrayList[Evaluate]
      list.add(evaluate1)
      list.add(evaluate2)
      list.add(evaluate3)
      list.add(evaluate4)
      list.add(evaluate5)
      //        list.add(evaluate6);

      val chain: EvaluateChain = new EvaluateChain(list)

      var historyData: HistoryData = historyDataState.value()
      if(historyData==null){
        historyData=new HistoryData()
      }
      chain.doEvaluate(historyData, evalauteData, evaluateReport)


//      println(evaluateReport+"****")
      //把评估报告放入到状态中
      //把evaluateReport转换成json
      val str: String = new ObjectMapper().writeValueAsString(evaluateReport)
//      println(str+"str")//1个问题
      evaluateReportState.put(evalauteData.getUuid,str)

      currentDayTotalLoginCountState.add(1)//每一次登录都被标记1次；无论登录成功与否，都算登录1次

      return evaluateReport
    }else{
      val loginSuccessData: LoginSuccessData = Util.parseLog2LoginSuccessData(log)

      //走更新链把登录成功的数据存入到历史数据中
      val update1: Update = new DeviceUpdate(10)
      val update2: Update = new CityUpdate
      val update3: Update = new InputFeaturesUpdate(10)
      val update4: Update = new PasswordUpdate
      val update5: Update = new TimeSlotUpdate

      val list: util.List[Update] = new util.ArrayList[Update]
      list.add(update1)
      list.add(update2)
      list.add(update3)
      list.add(update4)
      list.add(update5)

      val chain: UpdateChain = new UpdateChain(list)
      var historyData: HistoryData = historyDataState.value()
      if(historyData==null){
        historyData=new HistoryData()
      }

      chain.doUpdate(loginSuccessData, historyData)

      historyData.setLastLoginTime(loginSuccessData.getTime)
      historyData.setLastLoginGeoPoint(loginSuccessData.getGeoPoint)

      historyData.setCurrentDayLoginCount(currentDayTotalLoginCountState.get())

      historyDataState.update(historyData)
    }


    null
  }
}
