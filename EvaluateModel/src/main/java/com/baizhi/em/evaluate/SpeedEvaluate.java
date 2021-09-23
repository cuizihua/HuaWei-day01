package com.baizhi.em.evaluate;

import com.baizhi.em.entity.*;

/**
 * 位移速度的评估
 */
public class SpeedEvaluate extends Evaluate {

    private double threshold;//km/h

    public SpeedEvaluate(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public void eval(HistoryData historyData, EvalauteData evalauteData, EvaluateReport evaluateReport, EvaluateChain chain) {

        long lastLoginTime = historyData.getLastLoginTime();
        GeoPoint lastLoginGeoPoint = historyData.getLastLoginGeoPoint();

        long time = evalauteData.getTime();
        GeoPoint geoPoint = evalauteData.getGeoPoint();


        boolean isRisk = doEval(lastLoginGeoPoint,lastLoginTime,geoPoint,time);
        evaluateReport.setRisk(RiskFactor.SPEED,isRisk);

        chain.doEvaluate(historyData, evalauteData, evaluateReport);
    }

    private boolean doEval(GeoPoint lastLoginGeoPoint, long lastLoginTime, GeoPoint geoPoint, long time) {

        //1.根据两个地理位置完成位移的计算
        //2.根据两个时间完成时间差的计算

        double x11 = lastLoginGeoPoint.getLatitude();//角度单位的维度
        double y11 = lastLoginGeoPoint.getLongitude();//角度单位的经度

        double x22 = geoPoint.getLatitude();//角度单位的维度
        double y22 = geoPoint.getLongitude();//角度单位的经度


        //把角度单位的经纬度转换成弧度单位的经纬度
        double x1 = Math.toRadians(x11);
        double x2 = Math.toRadians(x22);
        double y1 = Math.toRadians(y11);
        double y2 = Math.toRadians(y22);

        double d = calcD(x1,y1,x2,y2);//距离:通过球面距离公式完成
        long t=time-lastLoginTime;//毫秒
        double t2 = t / 1000.0 / 60 / 60;
        double s = d / t2;


        return s>threshold;
    }

    /**
     *
     * 四个参数都是弧度单位
     * @param x1 第一个点的维度
     * @param y1 第一个点的经度
     * @param x2 第二个点的维度
     * @param y2 第二个点的经度
     * @return
     */
    private static double calcD(double x1,double y1,double x2,double y2){
        final double r=6371.393;
        return r*Math.acos(Math.sin(x1)*Math.sin(x2)+Math.cos(x1)*Math.cos(x2)*Math.cos(y1-y2));
    }


    //验证一下球面距离公式对不对

    public static void main(String[] args) {

        //郑州
        GeoPoint lastLoginGeoPoint=new GeoPoint(113.65,34.46);

        //北京
        GeoPoint geoPoint=new GeoPoint(116.4,39.9);

        double x11 = lastLoginGeoPoint.getLatitude();//角度单位的维度
        double y11 = lastLoginGeoPoint.getLongitude();//角度单位的经度

        double x22 = geoPoint.getLatitude();//角度单位的维度
        double y22 = geoPoint.getLongitude();//角度单位的经度


        //把角度单位的经纬度转换成弧度单位的经纬度
        double x1 = Math.toRadians(x11);
        double x2 = Math.toRadians(x22);
        double y1 = Math.toRadians(y11);
        double y2 = Math.toRadians(y22);

        double d = calcD(x1,y1,x2,y2);//距离:通过球面距离公式完成

        System.out.println(d);
    }
}
