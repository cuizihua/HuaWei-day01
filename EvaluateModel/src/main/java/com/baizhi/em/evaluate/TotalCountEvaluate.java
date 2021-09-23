package com.baizhi.em.evaluate;

import com.baizhi.em.entity.EvalauteData;
import com.baizhi.em.entity.EvaluateReport;
import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.RiskFactor;

/**
 * 当天累计登录次数评估
 * 如果当天累计登录次数超过了规定的值，就认定有风险
 */
public class TotalCountEvaluate  extends Evaluate{

    private int threshold;//阈值，规定的当天累计登录次数

    public TotalCountEvaluate(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void eval(HistoryData historyData, EvalauteData evalauteData, EvaluateReport evaluateReport, EvaluateChain chain) {

        //获取到当天累计登录次数
        Integer currentDayLoginCount = historyData.getCurrentDayLoginCount();

        evaluateReport.setRisk(RiskFactor.TOTAL,currentDayLoginCount>threshold);

        chain.doEvaluate(historyData, evalauteData, evaluateReport);

    }
}
