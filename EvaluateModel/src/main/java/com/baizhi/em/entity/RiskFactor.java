package com.baizhi.em.entity;

/**
 * 风险因子:里面列举出来了7个评估因子
 */
public enum  RiskFactor {
    //登录地区评估
    AREA("area"),
    //设备评估
    DEVICE("device"),
    //登录次数评估
    TOTAL("total"),
    //登录时间段评估--登录习惯
    TIMESLOT("timeslot"),
    //密码相似度评估
    SIMILARITY("similarity"),
    //输入特征评估
    INPUTFEATURE("inputfeature"),
    //位移速度评估
    SPEED("speed");


    private String name;
    RiskFactor(String name){
       this.name=name;
    }

    public String getName() {
        return name;
    }
}



