package com.baizhi.em.evaluate;

import com.baizhi.em.entity.EvalauteData;
import com.baizhi.em.entity.EvaluateReport;
import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.RiskFactor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 登录习惯评估
 * 本次登录，如果不符合用户的登录习惯，就认定有风险
 *
 * 如果用户还没有形成习惯，就认定没有风险
 *
 * 如果用户的累计登录次数超过了规定的值，才认定用户已经形成了习惯
 *
 * 本次的是时间==》星期，小时
 * 如果星期在历史登录中都没有登录过：本次登录时星期四，之前星期四从来没有登录过。不符合用户的登录习惯
 * 如何星期在历史登录中登录过：就可以根据星期获取到一个map<小时，次数>
 *     如果需要评估的小时在map<小时，次数>里面不存在，不符合用户的登录习惯
 *     如果需要评估的小时在map<小时，次数>里面存在，可以根据小时获取到对应的登录次数。
 *          map<小时，次数>中所有的登录次数进行升序排列;取三分之二位置处的值作为阈值进行判断。如果登录次数大于阈值认定没有风险
 */
public class TimeslotEvaluate extends Evaluate{
    @Override
    public void eval(HistoryData historyData, EvalauteData evalauteData, EvaluateReport evaluateReport, EvaluateChain chain) {

        boolean isRisk=doEval(historyData.getHistoryLoginTimeSlot(),evalauteData.getTime());
        evaluateReport.setRisk(RiskFactor.TIMESLOT,isRisk);

        chain.doEvaluate(historyData, evalauteData, evaluateReport);
    }


    private int threshold;//规定的阈值：如果累计登录次数超过了这个，才认定形成了登录习惯

    public TimeslotEvaluate(int threshold) {
        this.threshold = threshold;
    }

    private boolean doEval(Map<String, Map<String, Integer>> historyLoginTimeSlot, long time) {
        //累计登录次数的计算
        Stream<Map<String, Integer>> stream = historyLoginTimeSlot.values().stream();
        Stream<Collection<Integer>> collectionStream = stream.map(map -> map.values());
        Stream<Integer> integerStream = collectionStream.map(collection -> collection.stream().reduce((count1, count2) -> count1 + count2).get());
        Integer count = integerStream.reduce((e1, e2) -> e1 + e2).get();


        if(count>threshold){
            //形成了登录习惯

            //从time中获取到星期和小时
            /*Date date = new Date(time);
            int hours = date.getHours();
            int week = date.getDay();*/

            //Calendar.get(Calendar.DAY_OF_WEEK)
            Calendar calendar=Calendar.getInstance();//获取一个日历，当前时间对应的日历
            calendar.setTimeInMillis(time);//calendar日历就变成了time这个时间戳对应的日历

            String week = calendar.get(Calendar.DAY_OF_WEEK)+"";
            String hour = calendar.get(Calendar.HOUR_OF_DAY)+"";

            if(historyLoginTimeSlot.containsKey(week)){
                //星期登录过
                Map<String, Integer> map = historyLoginTimeSlot.get(week);
                if(map.containsKey(hour)){
                    //小时登录过
                    Integer count0 = map.get(hour);

                    List<Integer> list = map.values().stream().sorted().collect(Collectors.toList());
                    Integer threshold = list.get(list.size() * 2 / 3);
                    return count0<threshold;
                }else{
                    //小时没有登录过
                    return true;
                }
            }else{
                //星期没有登录过
                return true;
            }


        }/*else{
            return false;
        }
*/
        return false;
    }
}
