package com.baizhi.em.evaluate;

import com.baizhi.em.entity.EvalauteData;
import com.baizhi.em.entity.EvaluateReport;
import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.RiskFactor;

import java.util.Set;

/**
 * 登录地评估：
 * 如果第一次登录，就认定没有风险
 * 否则，本次登录的日志信息中的登录地，如果不在历史数据中，就认定有风险
 *
 * //第二个要写的是设备的风险评估
 * 如果第一次登录，就认定没有风险
 * 否则，本次登录的日志信息中的设备，如果不在历史数据中，就认定有风险
 */
public class CityEvaluate extends Evaluate {
    @Override
    public void eval(HistoryData historyData, EvalauteData evalauteData, EvaluateReport evaluateReport, EvaluateChain chain) {

        Set<String> historyCities = historyData.getHistoryCities();
        if(!(historyCities==null || historyCities.size()==0)){
            //不是第一次
            evaluateReport.setRisk(RiskFactor.AREA,!historyCities.contains(evalauteData.getCity()));
        }

        //放行
        chain.doEvaluate(historyData, evalauteData, evaluateReport);
    }
}
