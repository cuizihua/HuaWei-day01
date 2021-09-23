package com.baizhi.em.update;

import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.LoginSuccessData;

import java.util.HashSet;
import java.util.Set;

/**
 * 城市的更新：取出来登录成功的数据中的城市放入到历史数据中
 */
public class CityUpdate  extends Update{
    @Override
    public void update(LoginSuccessData loginSuccessData, HistoryData historyData, UpdateChain chain) {
        String city = loginSuccessData.getCity();
        Set<String> historyCities = historyData.getHistoryCities();
        if(historyCities==null){
            historyCities=new HashSet<>();
            //
            historyData.setHistoryCities(historyCities);
        }
        historyCities.add(city);



        chain.doUpdate(loginSuccessData, historyData);
    }
}
