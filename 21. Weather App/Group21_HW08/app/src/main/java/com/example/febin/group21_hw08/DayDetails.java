package com.example.febin.group21_hw08;

/**
 * Created by febin on 05/04/2017.
 */

public class DayDetails {

    int id;
    String date;
    String minimumTemp;
    String maximumTemp;
    String dayIcon;
    String dayPhrase;
    String nightIcon;
    String nightPhrase;
    String mobileLink;

    public String getMobileLink() {
        return mobileLink;
    }

    public void setMobileLink(String mobileLink) {
        this.mobileLink = mobileLink;
    }

    public DayDetails() {
    }

    @Override
    public String toString() {
        return "DayDetails{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", minimumTemp='" + minimumTemp + '\'' +
                ", maximumTemp='" + maximumTemp + '\'' +
                ", dayIcon='" + dayIcon + '\'' +
                ", dayPhrase='" + dayPhrase + '\'' +
                ", nightIcon='" + nightIcon + '\'' +
                ", nightPhrase='" + nightPhrase + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMinimumTemp() {
        return minimumTemp;
    }

    public void setMinimumTemp(String minimumTemp) {
        this.minimumTemp = minimumTemp;
    }

    public String getMaximumTemp() {
        return maximumTemp;
    }

    public void setMaximumTemp(String maximumTemp) {
        this.maximumTemp = maximumTemp;
    }

    public String getDayIcon() {
        return dayIcon;
    }

    public void setDayIcon(String dayIcon) {
        this.dayIcon = dayIcon;
    }

    public String getDayPhrase() {
        return dayPhrase;
    }

    public void setDayPhrase(String dayPhrase) {
        this.dayPhrase = dayPhrase;
    }

    public String getNightIcon() {
        return nightIcon;
    }

    public void setNightIcon(String nightIcon) {
        this.nightIcon = nightIcon;
    }

    public String getNightPhrase() {
        return nightPhrase;
    }

    public void setNightPhrase(String nightPhrase) {
        this.nightPhrase = nightPhrase;
    }
}
