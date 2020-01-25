package com.example.a001759.pregtrack.models;

public class ModelClassMomMerch {
    String imageUrl;
    String label;
    String price;
    String shopUrl;

    public ModelClassMomMerch() {
    }

    public ModelClassMomMerch(String imageUrl, String label, String price, String shopUrl) {
        this.imageUrl = imageUrl;
        this.label = label;
        this.price = price;
        this.shopUrl = shopUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }
}
