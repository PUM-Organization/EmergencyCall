package com.pomorganization.Models;

import java.util.Date;

/**
 * Created by Daniel on 5/19/2015.
 */
public class SensorsData {

    //return miliseconds from 197...
    private Long timeStamp;
    //medium value of accelerometer values
    private Double accMediumValue;
    //value from proximity sensor
    private Float proximityValue;

    public SensorsData(Long timeStamp, Double accMediumValue, Float proximityValue) {
        this.timeStamp = timeStamp;
        this.accMediumValue = accMediumValue;
        this.proximityValue = proximityValue;
    }

    public SensorsData(Float accX,Float accY,Float accZ, Float proximityValue) {
        this.accMediumValue = Math.sqrt(Math.pow(accX,2)+ Math.pow(accY,2) + Math.pow(accZ,2));
        this.proximityValue = proximityValue;
        this.timeStamp = new Date().getTime();
    }

    public Long getTimeStamp() {
        return timeStamp;
    }


    public Double getAccMediumValue() {
        return accMediumValue;
    }

    public Float getProximityValue() {
        return proximityValue;
    }


    @Override
    public String toString()
    {
        return "Sensor data " + getAccMediumValue() + "     time : " + getTimeStamp();
    }
}
