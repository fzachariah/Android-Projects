package com.example.febin.midterm;

import java.util.ArrayList;

/**
 * Created by febin on 20/03/2017.
 */

public class Cart {

    ArrayList<Product> cartList;
    String id;

    public ArrayList<Product> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<Product> cartList) {
        this.cartList = cartList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
