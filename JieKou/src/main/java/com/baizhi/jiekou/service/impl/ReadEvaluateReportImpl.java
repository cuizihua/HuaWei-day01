package com.baizhi.jiekou.service.impl;

import com.baizhi.jiekou.service.ReadEvaluateReport;
import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.queryablestate.client.QueryableStateClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

@Service
public class ReadEvaluateReportImpl implements ReadEvaluateReport {

    @Value("${flink.queryable.state.host}")
    private String host;
    @Value("${flink.queryable.state.port}")
    private int port;
    @Value("${flink.queryable.state.job-id}")
    private String jobId;
    @Value("${flink.queryable.state.name}")
    private String queryableStateName;

    @Override
    public String readEvaluateReport(String key, String uuid) {

        //连接可查询的状态所在的taskmanager（queryableStateServer===》9069）

        try {
            QueryableStateClient qsc=  new QueryableStateClient(host,port);
            JobID jobID=JobID.fromHexString(jobId);//79fc663dd72103a0efb2b02df56b3c55
            TypeInformation<String> keyTypeInfo=TypeInformation.of(String.class);

            MapStateDescriptor<String,String> mapStateDescriptor=new MapStateDescriptor<String, String>("msd",keyTypeInfo,keyTypeInfo);

            CompletableFuture<MapState<String, String>> kvState = qsc.getKvState(jobID, queryableStateName, key, keyTypeInfo, mapStateDescriptor);

            MapState<String, String> mapState = kvState.get();
            String evaluateReport = mapState.get(uuid);
            return evaluateReport;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
