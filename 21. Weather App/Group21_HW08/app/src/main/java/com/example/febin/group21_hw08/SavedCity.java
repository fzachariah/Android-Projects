package com.example.febin.group21_hw08;

/**
 * Created by febin on 06/04/2017.
 */

public class SavedCity {

    String key;
    String cityName;
    String country;
    String temperature;
    boolean favorites;
    String time;

    public SavedCity() {
    }

    @Override
    public String toString() {
        return "SavedCity{" +
                "key='" + key + '\'' +
                ", cityName='" + cityName + '\'' +
                ", country='" + country + '\'' +
                ", temperature='" + temperature + '\'' +
                ", favorites=" + favorites +
                '}';
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public boolean isFavorites() {
        return favorites;
    }

    public void setFavorites(boolean favorites) {
        this.favorites = favorites;
    }
}
