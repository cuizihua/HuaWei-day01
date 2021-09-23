package com.baizhi.em.update;

import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.LoginSuccessData;

import java.util.ArrayList;
import java.util.List;

public class DeviceUpdate extends Update {

    private int threshold;//存储多少条设备信息

    public DeviceUpdate(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void update(LoginSuccessData loginSuccessData, HistoryData historyData, UpdateChain chain) {
        //
        String device = loginSuccessData.getDevice();

        List<String> list = historyData.getHistoryDeviceInformations();

        if(list==null){
            list=new ArrayList<>();
            historyData.setHistoryDeviceInformations(list);
        }
        list.add(device);

        if(list.size()>threshold){
            list.remove(0);
        }

        chain.doUpdate(loginSuccessData, historyData);


    }
}
