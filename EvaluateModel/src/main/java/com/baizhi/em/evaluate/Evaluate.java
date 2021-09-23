package com.baizhi.em.evaluate;

import com.baizhi.em.entity.EvalauteData;
import com.baizhi.em.entity.EvaluateReport;
import com.baizhi.em.entity.HistoryData;

/**
 * 所有评估的抽象类
 */
public abstract class Evaluate {

    /**
     * 计算；做评估操作
     * @param historyData 历史数据
     * @param evalauteData 评估数据
     * @param evaluateReport 评估报告
     */
    public abstract  void eval(HistoryData historyData, EvalauteData evalauteData, EvaluateReport evaluateReport,EvaluateChain chain);

}
