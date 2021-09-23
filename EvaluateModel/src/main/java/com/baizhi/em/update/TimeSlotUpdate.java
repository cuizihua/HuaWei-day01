package com.baizhi.em.update;

import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.LoginSuccessData;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TimeSlotUpdate  extends Update{
    @Override
    public void update(LoginSuccessData loginSuccessData, HistoryData historyData, UpdateChain chain) {
        long time = loginSuccessData.getTime();

        Map<String, Map<String, Integer>> map = historyData.getHistoryLoginTimeSlot();

        if(map==null){
            map = new HashMap<>();
            historyData.setHistoryLoginTimeSlot(map);
        }

        Calendar calendar=Calendar.getInstance();//获取一个日历，当前时间对应的日历
        calendar.setTimeInMillis(time);//calendar日历就变成了time这个时间戳对应的日历

        String week = calendar.get(Calendar.DAY_OF_WEEK)+"";
        String hour = calendar.get(Calendar.HOUR_OF_DAY)+"";

        if(map.containsKey(week)){
            Map<String, Integer> map1 = map.get(week);
            if(map1.containsKey(hour)){
                map1.put(hour,map1.get(hour)+1);
            }else{
                map1.put(hour,1);
            }
        }else{
            Map<String, Integer> map1 = new HashMap<>();
            map1.put(hour,1);
            map.put(week,map1);
        }

        chain.doUpdate(loginSuccessData, historyData);




    }
}
