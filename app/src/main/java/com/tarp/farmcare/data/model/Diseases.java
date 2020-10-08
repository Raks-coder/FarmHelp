package com.tarp.farmcare.data.model;

import android.content.Context;
import android.content.SharedPreferences;

public class Diseases {

    private String name;
    private String type;
    private String description_en;
    private String description_pn;
    private String description_tn;
//    private String discription;
    private int id;




    public Diseases() {

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

//    public String getDiscription() {
//        return discription;
//    }

//    public void setDiscription(String discription) {
//        this.discription = discription;
//    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getDescription_en() {
        return description_en;
    }

    public String getDescription_pn() {
        return description_pn;
    }

    public String getDescription_tn() {
        return description_tn;
    }

    public void setDescription_pn(String description_pu) {
        this.description_pn = description_pu;
    }

    public void setDescription_tn(String description_tn) {
        this.description_tn = description_tn;
    }



}
