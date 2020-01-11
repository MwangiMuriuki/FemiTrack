package com.example.a001759.pregtrack.models;

public class ModelClassUsers {

    String uName;
    String email;
    String display_picture;
    String userID;
    String due_date;
    String weeks_pregnant;

    public ModelClassUsers() {
    }

    public ModelClassUsers(String uName, String email, String display_picture, String userID, String due_date, String weeks_pregnant) {
        this.uName = uName;
        this.email = email;
        this.display_picture = display_picture;
        this.userID = userID;
        this.due_date = due_date;
        this.weeks_pregnant = weeks_pregnant;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplay_picture() {
        return display_picture;
    }

    public void setDisplay_picture(String display_picture) {
        this.display_picture = display_picture;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getWeeks_pregnant() {
        return weeks_pregnant;
    }

    public void setWeeks_pregnant(String weeks_pregnant) {
        this.weeks_pregnant = weeks_pregnant;
    }
}
