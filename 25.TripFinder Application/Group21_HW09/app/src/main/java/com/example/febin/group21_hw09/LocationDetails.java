package com.example.febin.group21_hw09;

import java.io.Serializable;

/**
 * Created by febin on 25/04/2017.
 */

public class LocationDetails implements Serializable {

    String id;
    String locationName;
    String lat;
    String longt;

    public LocationDetails() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    @Override
    public String toString() {
        return "LocationDetails{" +
                "id='" + id + '\'' +
                ", locationName='" + locationName + '\'' +
                ", lat='" + lat + '\'' +
                ", longt='" + longt + '\'' +
                '}';
    }
}
