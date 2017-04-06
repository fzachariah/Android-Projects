package com.example.febin.midterm;

import java.io.Serializable;

/**
 * Created by febin on 20/03/2017.
 */

public class Product implements Serializable {

    String id;
    String title;
    String price;
    String msrpPrcie;
    String discount;
    String imageURLOne;
    String imageURLTwo;

    boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", msrpPrcie='" + msrpPrcie + '\'' +
                ", discount='" + discount + '\'' +
                ", imageURLOne='" + imageURLOne + '\'' +
                ", imageURLTwo='" + imageURLTwo + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMsrpPrcie() {
        return msrpPrcie;
    }

    public void setMsrpPrcie(String msrpPrcie) {
        this.msrpPrcie = msrpPrcie;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImageURLOne() {
        return imageURLOne;
    }

    public void setImageURLOne(String imageURLOne) {
        this.imageURLOne = imageURLOne;
    }

    public String getImageURLTwo() {
        return imageURLTwo;
    }

    public void setImageURLTwo(String imageURLTwo) {
        this.imageURLTwo = imageURLTwo;
    }
}
