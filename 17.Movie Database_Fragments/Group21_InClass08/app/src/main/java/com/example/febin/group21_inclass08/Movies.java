package com.example.febin.group21_inclass08;

import java.io.Serializable;

/**
 * Created by febin on 27/01/2017.
 */

public class Movies implements Serializable,Comparable<Movies> {
    private String name;
    private String description;
    private String genre;
    private int rating;
    private int year;
    private String link;

    public Movies(String name, String description, String genre, int rating, int year, String link) {
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.year = year;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", rating=" + rating +
                ", year=" + year +
                ", link='" + link + '\'' +
                '}';
    }

    @Override
    public int compareTo(Movies o) {
        return this.getYear()-(o.getYear());
    }
}

