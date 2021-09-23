package com.baizhi.em.evaluate;


import com.baizhi.em.entity.EvalauteData;
import com.baizhi.em.entity.EvaluateReport;
import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.RiskFactor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 输入特征评估：通过欧式距离完成
 */
public class InputFeatureEvaluate extends Evaluate {
    @Override
    public void eval(HistoryData historyData, EvalauteData evalauteData, EvaluateReport evaluateReport, EvaluateChain chain) {


        List<double[]> latestInputFeatures = historyData.getLatestInputFeatures();

        double[] inputFeatures = evalauteData.getInputFeatures();

        boolean isRisk = doEval(latestInputFeatures,inputFeatures);

        evaluateReport.setRisk(RiskFactor.INPUTFEATURE,isRisk);

        chain.doEvaluate(historyData, evalauteData, evaluateReport);

    }

    //做评估操作
    private boolean doEval(List<double[]> latestInputFeatures, double[] inputFeatures) {


        if(latestInputFeatures==null||latestInputFeatures.size()==0){
            return false;
        }

        //计算圆心点
        double[] yuanXinDian=new double[latestInputFeatures.get(0).length];
        double[] sum=new double[latestInputFeatures.get(0).length];

        for (double[] latestInputFeature : latestInputFeatures) {
            for (int i = 0; i < latestInputFeature.length; i++) {
                sum[i]+=latestInputFeature[i];
            }
        }

        for (int i = 0; i < sum.length; i++) {
            yuanXinDian[i]=sum[i]/latestInputFeatures.size();
        }


        //计算latestInputFeatures中的每一个元素到圆心点的欧式距离
        List<Double> list = latestInputFeatures.stream().map(d -> calcEuclideanMetric(d, yuanXinDian)).sorted().collect(Collectors.toList());

        //获取到阈值
        Double threshold = list.get(list.size() * 2 / 3);

        //计算inputFeatures到圆心点的欧式距离
        double d = calcEuclideanMetric(inputFeatures, yuanXinDian);


        //计算出来的距离（评估数据到圆心点的距离）大于了规定的阈值，就认定有风险
        return d>threshold;
    }

    /**计算欧式距离
     *
     * @param x n维空间的一个点
     * @param y n维空间的另一个点
     * @return 欧式距离
     */
    private double calcEuclideanMetric(double[] x,double[] y){

        double sum=0;
        for (int i = 0; i < x.length; i++) {
            sum+=Math.pow(x[i]-y[i],2);
        }

        return Math.sqrt(sum);

    }


}
