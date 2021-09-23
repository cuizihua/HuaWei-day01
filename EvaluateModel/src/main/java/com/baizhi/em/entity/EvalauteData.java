package com.baizhi.em.entity;

import java.util.Arrays;

/**
 * 评估数据：对应评估日志信息
 */
public class EvalauteData {
    private long time;//时间戳
    private String appName;//应用名
    private String username;//用户名
    private String uuid;//唯一标记
    private String ordernessPassword;//乱序密码
    private String city;//登录地
    private GeoPoint geoPoint;//地理位置
    private double[] inputFeatures;//输入特征：每一个输入框用户在输入数据的时候，花费了多长时间
    private String device;//设备信息

    @Override
    public String toString() {
        return "EvalauteData{" +
                "time=" + time +
                ", appName='" + appName + '\'' +
                ", username='" + username + '\'' +
                ", uuid='" + uuid + '\'' +
                ", ordernessPassword='" + ordernessPassword + '\'' +
                ", city='" + city + '\'' +
                ", geoPoint=" + geoPoint +
                ", inputFeatures=" + Arrays.toString(inputFeatures) +
                ", device='" + device + '\'' +
                '}';
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAppName() {
        return appName;
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

    public String getOrdernessPassword() {
        return ordernessPassword;
    }

    public void setOrdernessPassword(String ordernessPassword) {
        this.ordernessPassword = ordernessPassword;
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

    public double[] getInputFeatures() {
        return inputFeatures;
    }

    public void setInputFeatures(double[] inputFeatures) {
        this.inputFeatures = inputFeatures;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public EvalauteData() {
    }

    public EvalauteData(long time, String appName, String username, String uuid, String ordernessPassword, String city, GeoPoint geoPoint, double[] inputFeatures, String device) {
        this.time = time;
        this.appName = appName;
        this.username = username;
        this.uuid = uuid;
        this.ordernessPassword = ordernessPassword;
        this.city = city;
        this.geoPoint = geoPoint;
        this.inputFeatures = inputFeatures;
        this.device = device;
    }
}
