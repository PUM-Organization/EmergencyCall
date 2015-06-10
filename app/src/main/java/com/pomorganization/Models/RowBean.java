package com.pomorganization.Models;

import org.json.JSONObject;

/**
 * Created by Daniel on 6/10/2015.
 * Model for contact
 */
public class RowBean {
    private String name;
    private String phoneNumber;

    public RowBean() {
    }

    public RowBean(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
