package com.baizhi.em.update;

import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.LoginSuccessData;

import java.util.ArrayList;
import java.util.List;

public class InputFeaturesUpdate extends Update{


    private int threshold;//存储的输入特征的个数，存最近的多少条数据

    public InputFeaturesUpdate(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void update(LoginSuccessData loginSuccessData, HistoryData historyData, UpdateChain chain) {
        double[] inputFeatures = loginSuccessData.getInputFeatures();

        List<double[]> list = historyData.getLatestInputFeatures();

        if(list==null){
            list=new ArrayList<>();
            historyData.setLatestInputFeatures(list);
        }

        list.add(inputFeatures);

        if(list.size()>threshold){
            list.remove(0);
        }

        chain.doUpdate(loginSuccessData, historyData);


    }
}
