package com.example.febin.group21_hw09;

import java.io.Serializable;

/**
 * Created by febin on 21/04/2017.
 */

public class MessageDetails implements Serializable {

    String message;
    int type;
    String senderId;
    String senderName;
    long time;

    public MessageDetails() {
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "MessageDetails{" +
                "message='" + message + '\'' +
                ", type=" + type +
                ", senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", time=" + time +
                '}';
    }
}
