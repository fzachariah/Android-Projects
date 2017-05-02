package com.example.febin.group21_hw09;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by febin on 21/04/2017.
 */

public class TripDetails implements Serializable {

    String tripId;
    String title;
    String photoURL;
    String createdBy;
    ArrayList<String> members=new ArrayList<>();
    ArrayList<MessageDetails> messageDetailsArrayList=new ArrayList<>();
    ArrayList<LocationDetails> locationDetailsArrayList=new ArrayList<>();

    public TripDetails() {
    }



    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public ArrayList<MessageDetails> getMessageDetailsArrayList() {
        return messageDetailsArrayList;
    }

    public void setMessageDetailsArrayList(ArrayList<MessageDetails> messageDetailsArrayList) {
        this.messageDetailsArrayList = messageDetailsArrayList;
    }


    public ArrayList<LocationDetails> getLocationDetailsArrayList() {
        return locationDetailsArrayList;
    }

    public void setLocationDetailsArrayList(ArrayList<LocationDetails> locationDetailsArrayList) {
        this.locationDetailsArrayList = locationDetailsArrayList;
    }

    @Override
    public String toString() {
        return "TripDetails{" +
                "tripId='" + tripId + '\'' +
                ", title='" + title + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", members=" + members +
                ", messageDetailsArrayList=" + messageDetailsArrayList +
                ", locationDetailsArrayList=" + locationDetailsArrayList +
                '}';
    }
}
