package com.example.se171676_finalproject.Domain;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemsDomain implements Serializable {
    private String id;
    private String title;
    private String description;
    private ArrayList<String> picUrl;
    private double price;
    private double oldPrice;
    private int review;
    private double rating;
    private int numberInCart;

    public ItemsDomain() {
    }

    public ItemsDomain(String description, double oldPrice, ArrayList<String> picUrl, double price, double rating, int review, String title) {
        this.description = description;
        this.oldPrice = oldPrice;
        this.picUrl = picUrl;
        this.price = price;
        this.rating = rating;
        this.review = review;
        this.title = title;
    }
    public ItemsDomain(String id, String title, String description, double price, double oldPrice, double rating, int review, ArrayList<String> picUrl) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.oldPrice = oldPrice;
        this.rating = rating;
        this.review = review;
        this.picUrl = picUrl != null ? picUrl : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
