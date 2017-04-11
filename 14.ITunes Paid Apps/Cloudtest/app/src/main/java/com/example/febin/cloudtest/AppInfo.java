package com.example.febin.cloudtest;

import java.io.Serializable;

/**
 * Created by febin on 20/02/2017.
 */

public class AppInfo implements Serializable,Comparable<AppInfo> {

    private String imageURL;
    private String appName;
    private String devName;
    private String releaseDate;
    private String price;
    private String category;
    private String savedURL;
    int status;


    public String getSavedURL() {
        return savedURL;
    }

    public void setSavedURL(String savedURL) {
        this.savedURL = savedURL;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "imageURL='" + imageURL + '\'' +
                ", appName='" + appName + '\'' +
                ", devName='" + devName + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", price='" + price + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int compareTo(AppInfo o) {
        String compareThis=this.price.replaceAll("USD","").trim();
        String compareO=o.price.replaceAll("USD","").trim();

        return (int) (Float.parseFloat(compareThis)-Float.parseFloat(compareO));
    }
}
