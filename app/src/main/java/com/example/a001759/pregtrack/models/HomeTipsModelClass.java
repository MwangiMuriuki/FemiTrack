package com.example.a001759.pregtrack.models;

public class HomeTipsModelClass {

    String image;
    String title;
    String info;

    public HomeTipsModelClass() {
    }

    public HomeTipsModelClass(String image, String title, String info) {
        this.image = image;
        this.title = title;
        this.info = info;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
