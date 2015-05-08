package com.swach.dto;

/**
 * Created by krraje on 29/04/15.
 */
public class TwitterData {

   private String location;
   private String  tweetId;
   private String  tweetText;
   private double latitude;
   private double longitude;
   private String username;
   private int dirtynessMeter;



    public TwitterData() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDirtynessMeter() {
        return dirtynessMeter;
    }

    public void setDirtynessMeter(int dirtynessMeter) {
        this.dirtynessMeter = dirtynessMeter;
    }

    @Override
    public String toString() {
        return "TwitterData{" +
                "location='" + location + '\'' +
                ", tweetId='" + tweetId + '\'' +
                ", tweetText='" + tweetText + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", username='" + username + '\'' +
                ", dirtynessMeter='" + dirtynessMeter + '\'' +
                '}';
    }
}
