package com.example.febin.group21_hw05;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by febin on 16/02/2017.
 */

public class SingleGame implements Serializable {

    String id;
    String overView;
    String title;
    ArrayList<String> genreList=new ArrayList<>();
    String publisher;
    String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "SingleGame{" +
                "id='" + id + '\'' +
                ", overView='" + overView + '\'' +
                ", title='" + title + '\'' +
                ", genreList=" + genreList +
                ", publisher='" + publisher + '\'' +
                ", videoURL='" + videoURL + '\'' +
                ", similarGames=" + similarGames +
                '}';
    }

    public ArrayList<String> getGenreList() {
        return genreList;
    }

    public void setGenreList(ArrayList<String> genreList) {
        this.genreList = genreList;
    }

    String videoURL;
    ArrayList<String> similarGames=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public ArrayList<String> getSimilarGames() {
        return similarGames;
    }

    public void setSimilarGames(ArrayList<String> similarGames) {
        this.similarGames = similarGames;
    }
}
