package com.baizhi.em.entity;

public class GeoPoint {
    private double longitude;//经度
    private double latitude;//维度

    @Override
    public String toString() {
        return "GeoPoint{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public GeoPoint() {
    }

    public GeoPoint(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
