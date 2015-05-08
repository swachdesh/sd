package com.swach.dto;

/**
 * Created by krraje on 28/04/15.
 */
public class LatitudeLongitude {


    private double latitude;
    private double longitude;
    private String location;
    public LatitudeLongitude(double latitude, double longitude, String location) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public String toString() {
        return "LatitudeLongitude{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", location='" + location + '\'' +
                '}';
    }
}
