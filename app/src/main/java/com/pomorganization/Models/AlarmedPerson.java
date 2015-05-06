package com.pomorganization.Models;

/**
 * Created by Daniel on 5/6/2015.
 */
public class AlarmedPerson extends Entity {
    private String PhoneNumber;
    private String Name;

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
