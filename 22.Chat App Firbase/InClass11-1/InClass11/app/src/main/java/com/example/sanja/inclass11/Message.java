package com.example.sanja.inclass11;

import java.util.ArrayList;

/**
 * Created by sanja on 4/10/2017.
 */

public class Message {

    String email;
    String dispName;
    String message;
    int type;
    String time;

    @Override
    public String toString() {
        return "Message{" +
                "email='" + email + '\'' +
                ", dispName='" + dispName + '\'' +
                ", message='" + message + '\'' +
                ", type=" + type +
                ", time='" + time + '\'' +
                '}';
    }

    ArrayList<Message> messageArrayList=new ArrayList<>();

    public ArrayList<Message> getMessageArrayList() {
        return messageArrayList;
    }

    public void setMessageArrayList(ArrayList<Message> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }

    public Message() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


