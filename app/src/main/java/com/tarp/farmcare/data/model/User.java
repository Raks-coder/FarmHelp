package com.tarp.farmcare.data.model;

public class User {
    public String name, email;
    public String latitude, longitude;
    public String calamity;


    public User() {

    }
    public User(String name, String email, String latitude, String longitude, String calamity) {
        this.name = name;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.calamity = calamity;

    }

    public User(String latitude, String longitude, String calamity) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.calamity = calamity;

    }

    public User(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.calamity = calamity;

    }

    public void setInfo(String name, String email) {
        this.name = name;
        this.email = email;

    }

    public void setLocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }

}
