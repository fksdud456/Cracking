package com.example.student.crackingtablet;

public class Location1 {


    String id;
    String lon;
    String lat;

    Location1(){

    }

    Location1(String id, String lon, String lat){
        this.id = id;
        this.lon = lon;
        this.lat = lat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
