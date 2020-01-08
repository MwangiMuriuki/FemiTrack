package com.example.a001759.pregtrack.models;

public class ModelClassWeeklyCalendar {

    String week_picture;
    String week_number;
    String baby_info;
    String symptoms;
    String source;

    public ModelClassWeeklyCalendar() {
    }

    public ModelClassWeeklyCalendar(String week_picture, String week_number, String baby_info, String symptoms, String source) {
        this.week_picture = week_picture;
        this.week_number = week_number;
        this.baby_info = baby_info;
        this.symptoms = symptoms;
        this.source = source;
    }

    public String getWeek_picture() {
        return week_picture;
    }

    public void setWeek_picture(String week_picture) {
        this.week_picture = week_picture;
    }

    public String getWeek_number() {
        return week_number;
    }

    public void setWeek_number(String week_number) {
        this.week_number = week_number;
    }

    public String getBaby_info() {
        return baby_info;
    }

    public void setBaby_info(String baby_info) {
        this.baby_info = baby_info;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
