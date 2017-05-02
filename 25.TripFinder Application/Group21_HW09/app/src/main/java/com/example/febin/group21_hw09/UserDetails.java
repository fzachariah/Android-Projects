package com.example.febin.group21_hw09;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by febin on 18/04/2017.
 */

public class UserDetails implements Serializable {

    String id;
    String firstName;
    String lastName;
    String gender;
    String email;
    String password;
    String photoUrl;

    ArrayList<String> friendList=new ArrayList<>();
    ArrayList<String> receivedList=new ArrayList<>();
    ArrayList<String> sentList=new ArrayList<>();
    ArrayList<TripDetails> tripList=new ArrayList<>();

    public ArrayList<TripDetails> getTripList() {
        return tripList;
    }

    public void setTripList(ArrayList<TripDetails> tripList) {
        this.tripList = tripList;
    }

    public ArrayList<String> getSentList() {
        return sentList;
    }

    public void setSentList(ArrayList<String> sentList) {
        this.sentList = sentList;
    }

    public UserDetails() {
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", friendList=" + friendList +
                ", receivedList=" + receivedList +
                ", sentList=" + sentList +
                ", tripList=" + tripList +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

    public ArrayList<String> getReceivedList() {
        return receivedList;
    }

    public void setReceivedList(ArrayList<String> receivedList) {
        this.receivedList = receivedList;
    }


}
