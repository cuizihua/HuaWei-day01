package com.baizhi.flink.state.controller;

import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.queryablestate.client.QueryableStateClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class QueryableContoller {

//    @RequestMapping("/queryableState")
    //RestFull 风格
    @RequestMapping("/queryableState/{keyyyyyy}")
    public Integer queryableState(@PathVariable("keyyyyyy") String key) throws  Exception{


        QueryableStateClient qsc=new QueryableStateClient("hadoop10",9069);

        JobID jobID=JobID.fromHexString("79fc663dd72103a0efb2b02df56b3c55");//79fc663dd72103a0efb2b02df56b3c55
        String queryableStateName="word-count-queryable-state";//flink代码里面设置的那个名字
//        String key="a";
        TypeInformation<String> keyTypeInfo=TypeInformation.of(String.class);
        ReducingStateDescriptor<Integer> rsd=new ReducingStateDescriptor<Integer>("rsd", new ReduceFunction<Integer>() {
            @Override
            public Integer reduce(Integer value1, Integer value2) throws Exception {
                return value1+value2;
            }
        }, TypeInformation.of(Integer.class));
        CompletableFuture<ReducingState<Integer>> completableFuture = qsc.getKvState(jobID, queryableStateName, key, keyTypeInfo, rsd);

        ReducingState<Integer> reducingState = completableFuture.get();

        Integer count = reducingState.get();

        return count;
    }
}
