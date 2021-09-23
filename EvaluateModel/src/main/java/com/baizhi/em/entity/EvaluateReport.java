package com.baizhi.em.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * 评估报告类型：包含了基础信息以及七个Boolean类型的指标
 */
public class EvaluateReport {

    private String appName;
    private String username;
    private String uuid;
    private long time;
    private String city;
    private GeoPoint geoPoint;

    //存储7个评估因子以及对应的值
    private Map<RiskFactor,Boolean> risk = new HashMap<>();

    public EvaluateReport(String appName, String username, String uuid, long time, String city, GeoPoint geoPoint) {
        this.appName = appName;
        this.username = username;
        this.uuid = uuid;
        this.time = time;
        this.city = city;
        this.geoPoint = geoPoint;

        //7个评估因子的初始化
        risk.put(RiskFactor.AREA,false);
        risk.put(RiskFactor.DEVICE,false);
        risk.put(RiskFactor.INPUTFEATURE,false);
        risk.put(RiskFactor.SIMILARITY,false);
        risk.put(RiskFactor.TIMESLOT,false);
        risk.put(RiskFactor.TOTAL,false);
        risk.put(RiskFactor.SPEED,false);
    }

    public String getAppName() {
        return appName;
    }

    public void setRisk(RiskFactor riskFactor,Boolean isRisk){
        risk.put(riskFactor, isRisk);
    }

    public Map<RiskFactor, Boolean> getRisk() {
        return risk;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public EvaluateReport() {
    }

    /**
     * 把基础信息，用空格隔开
     * WebApplication zhangsan  212223320003,222
     * 7个评估因子对应的值也用空格隔开==》按照评估因子的name进行排序
     *
     * 评估因子的顺序
     * 城市 设备 输入特征 密码相似度 位移速度 登录习惯 当天累计登录次数
     * @return
     */
    @Override
    public String toString() {


        Set<RiskFactor> allKeys = risk.keySet();
        Stream<RiskFactor> sorted = allKeys.stream().sorted((RiskFactor rf1, RiskFactor rf2) -> rf1.getName().compareTo(rf2.getName()));
        Stream<Boolean> booleanStream = sorted.map(e -> risk.get(e));
        Stream<String> stringStream = booleanStream.map(e -> e + "");
        Optional<String> optional = stringStream.reduce((e1, e2) -> e1 + " " + e2);
        String riskFatorStr = optional.get();

        String str=appName+" "+username+" "+ uuid+" "+time+" "+city+" "+geoPoint+" "+riskFatorStr;

        return str;


    }
}
