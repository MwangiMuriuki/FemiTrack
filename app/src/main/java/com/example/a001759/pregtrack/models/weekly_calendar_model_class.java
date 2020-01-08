package com.example.a001759.pregtrack.models;

public class weekly_calendar_model_class {

    String week_number;
    String image;

    public weekly_calendar_model_class() {
    }

    public weekly_calendar_model_class(String week_number, String image) {
        this.week_number = week_number;
        this.image = image;
    }

    public String getWeek_number() {
        return week_number;
    }

    public void setWeek_number(String week_number) {
        this.week_number = week_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
