package com.tsqc.util;

/**
 * Created by someo on 11-03-2017.
 */

public class ProductItem {

    String name;
    String price;
    int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public ProductItem(String id, String name, String price) {

        this.id=id;
        this.name=name;
        this.price=price;
    }

    public ProductItem(String name, String price, int status) {

        this.name=name;
        this.price=price;
        this.status=status;
    }

    public ProductItem(String id, String name, String price, int status) {

        this.id=id;
        this.name=name;
        this.price=price;
        this.status=status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
}
