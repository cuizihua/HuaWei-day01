package com.baizhi.em.evaluate;

import com.baizhi.em.entity.EvalauteData;
import com.baizhi.em.entity.EvaluateReport;
import com.baizhi.em.entity.HistoryData;
import com.baizhi.em.entity.RiskFactor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 密码相似性评估：通过余弦相似完成
 *
 * 1.把密码转换成向量
 * 2.通过余弦相似性公式计算评估密码和每一个使用过的密码的相似性
 * 3.如果有一个余弦相似性超过规定的值，就认定没有风险
 */
public class SimilirtyEvaluate extends Evaluate {

    private double threshold;

    public SimilirtyEvaluate(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public void eval(HistoryData historyData, EvalauteData evalauteData, EvaluateReport evaluateReport, EvaluateChain chain) {

        Set<String> historypasswords = historyData.getHistorypasswords();
        String ordernessPassword = evalauteData.getOrdernessPassword();

        boolean isRisk = doEval(historypasswords,ordernessPassword);
        evaluateReport.setRisk(RiskFactor.SIMILARITY,isRisk);

        chain.doEvaluate(historyData, evalauteData, evaluateReport);

    }

    //计算是否有风险的
    private boolean doEval(Set<String> historypasswords, String ordernessPassword) {

        //需要把每一个密码转换成向量
        //1. 获取到所有使用过的字符，包括现在要评估的数据在内==》set<Charator>
        //2. 判断set集合中的每一个字符，在密码中出现的次数===》这个密码对应的向量--->Integer[],数组的长度就是set.size()

        if(historypasswords==null || historypasswords.size()==0){
            return false;
        }
        Set<Character> wordBag = new HashSet<>();

        for (String historypassword : historypasswords) {
            char[] chars = historypassword.toCharArray();
            for (char aChar : chars) {
                wordBag.add(aChar);
            }
        }
        char[] chars = ordernessPassword.toCharArray();
        for (char aChar : chars) {
            wordBag.add(aChar);
        }

        //把ordernessPassword转换成向量
        Integer[] vector = parseString2Vector(wordBag, ordernessPassword);

        //把所有用过的密码转换成向量
        List<Integer[]> vectors = historypasswords.stream().map(e -> parseString2Vector(wordBag, e)).collect(Collectors.toList());


        //计算余弦相似性
        List<Double> collect = vectors.stream().map(e -> calcSimilarity(e, vector)).filter(e -> e > threshold).collect(Collectors.toList());

        return collect.size()==0;
    }

    //解析字符串，转换成对应的向量
    private Integer[] parseString2Vector(Set<Character> wordBag,String password){
        Integer[] vector=new Integer[wordBag.size()];

        //wordBag中的每一个字符，在password里面出现的次数放入到数组vector里面

        char[] chars = password.toCharArray();//字符串转换成字符数组
        int index=0;
        for (Character character : wordBag) {
            int count=0;
            for (char aChar : chars) {
                if(character==aChar){
                    count++;
                }
            }
            vector[index]=count;

            index++;
        }

        return vector;
    }

    //计算两个向量的余弦值：使用余弦相似性完成
    //
    private double calcSimilarity(Integer[] a,Integer[] b){

        //计算公式中的分子
        int fenZi=0;
        for (int i = 0; i < a.length; i++) {
            fenZi+= a[i]*b[i];
        }


        //计算公式中的分母
        int sumA=0;
        for (Integer integer : a) {
            sumA+=integer*integer;
        }
        int sumB=0;
        for (Integer integer : b) {
            sumB+=integer*integer;
        }

        double fenMu = Math.sqrt(sumA) * Math.sqrt(sumB);


        return fenZi/fenMu;
    }
}
