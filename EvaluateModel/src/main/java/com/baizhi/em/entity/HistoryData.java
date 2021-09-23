package com.baizhi.em.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HistoryData {
    //记录用户历史登录城市
    private Set<String> historyCities;
    //记录用户历史登录设备
    private List<String> historyDeviceInformations;
    //记录用户当天登录次数
    private Integer currentDayLoginCount;
    //记录用户历史登录习惯:key是星期，value是map(key是小时，value是登录次数)
    private Map<String, Map<String,Integer>> historyLoginTimeSlot;
    //记录用户历史密码
    private Set<String> historypasswords;
    //记录用户历史输入特征
    private List<double[]> latestInputFeatures;
    //记录最近一次历史登录时间
    private long lastLoginTime;
    //记录最近一次历史登录经纬度
    private GeoPoint lastLoginGeoPoint;

    @Override
    public String toString() {
        return "HistoryData{" +
                "historyCities=" + historyCities +
                ", historyDeviceInformations=" + historyDeviceInformations +
                ", currentDayLoginCount=" + currentDayLoginCount +
                ", historyLoginTimeSlot=" + historyLoginTimeSlot +
                ", historypasswords=" + historypasswords +
                ", latestInputFeatures=" + latestInputFeatures +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginGeoPoint=" + lastLoginGeoPoint +
                '}';
    }

    public Set<String> getHistoryCities() {
        return historyCities;
    }

    public void setHistoryCities(Set<String> historyCities) {
        this.historyCities = historyCities;
    }

    public List<String> getHistoryDeviceInformations() {
        return historyDeviceInformations;
    }

    public void setHistoryDeviceInformations(List<String> historyDeviceInformations) {
        this.historyDeviceInformations = historyDeviceInformations;
    }

    public Integer getCurrentDayLoginCount() {
        return currentDayLoginCount;
    }

    public void setCurrentDayLoginCount(Integer currentDayLoginCount) {
        this.currentDayLoginCount = currentDayLoginCount;
    }

    public Map<String, Map<String, Integer>> getHistoryLoginTimeSlot() {
        return historyLoginTimeSlot;
    }

    public void setHistoryLoginTimeSlot(Map<String, Map<String, Integer>> historyLoginTimeSlot) {
        this.historyLoginTimeSlot = historyLoginTimeSlot;
    }

    public Set<String> getHistorypasswords() {
        return historypasswords;
    }

    public void setHistorypasswords(Set<String> historypasswords) {
        this.historypasswords = historypasswords;
    }

    public List<double[]> getLatestInputFeatures() {
        return latestInputFeatures;
    }

    public void setLatestInputFeatures(List<double[]> latestInputFeatures) {
        this.latestInputFeatures = latestInputFeatures;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public GeoPoint getLastLoginGeoPoint() {
        return lastLoginGeoPoint;
    }

    public void setLastLoginGeoPoint(GeoPoint lastLoginGeoPoint) {
        this.lastLoginGeoPoint = lastLoginGeoPoint;
    }

    public HistoryData() {
    }

    public HistoryData(Set<String> historyCities, List<String> historyDeviceInformations, Integer currentDayLoginCount, Map<String, Map<String, Integer>> historyLoginTimeSlot, Set<String> historypasswords, List<double[]> latestInputFeatures, long lastLoginTime, GeoPoint lastLoginGeoPoint) {
        this.historyCities = historyCities;
        this.historyDeviceInformations = historyDeviceInformations;
        this.currentDayLoginCount = currentDayLoginCount;
        this.historyLoginTimeSlot = historyLoginTimeSlot;
        this.historypasswords = historypasswords;
        this.latestInputFeatures = latestInputFeatures;
        this.lastLoginTime = lastLoginTime;
        this.lastLoginGeoPoint = lastLoginGeoPoint;
    }
}
