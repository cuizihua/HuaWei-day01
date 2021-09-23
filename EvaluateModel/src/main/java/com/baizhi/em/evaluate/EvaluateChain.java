package com.baizhi.em.evaluate;

import com.baizhi.em.entity.EvalauteData;
import com.baizhi.em.entity.EvaluateReport;
import com.baizhi.em.entity.HistoryData;

import java.util.List;

/**
 * 评估链，把所有的评估因子封装起来形成一个链条
 */
public class EvaluateChain {

    //所有的评估
    private List<Evaluate> allEvaluates;

    private int position;//评估链执行到了哪个位置

    public EvaluateChain(List<Evaluate> allEvaluates) {
        this.allEvaluates = allEvaluates;
    }

    /**
     * 让评估链继续下一个责任的执行
     * @param historyData 历史数据
     * @param evalauteData 评估数据
     * @param evaluateReport 评估报告
     */
    public void doEvaluate(HistoryData historyData, EvalauteData evalauteData, EvaluateReport evaluateReport){

        //如果链条还没有走到最后，就获取到下一个责任，继续执行
        if(position<allEvaluates.size()){
            Evaluate evaluate = allEvaluates.get(position);
            position++;//3
            evaluate.eval(historyData,evalauteData,evaluateReport,this);
        }

    }
}
