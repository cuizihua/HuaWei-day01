package com.baizhi.em.update;

import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.LoginSuccessData;

import java.util.HashSet;
import java.util.Set;

public class PasswordUpdate  extends Update{
    @Override
    public void update(LoginSuccessData loginSuccessData, HistoryData historyData, UpdateChain chain) {
        String ordernessPassword = loginSuccessData.getOrdernessPassword();
        Set<String> historypasswords = historyData.getHistorypasswords();
        if(historypasswords==null){
            historypasswords=new HashSet<>();
            historyData.setHistorypasswords(historypasswords);
        }
        historypasswords.add(ordernessPassword);

        chain.doUpdate(loginSuccessData, historyData);
    }
}
