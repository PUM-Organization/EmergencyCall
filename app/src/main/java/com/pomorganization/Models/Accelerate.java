package com.pomorganization.Models;



/**
 * Created by Daniel on 5/6/2015.
 */
public class Accelerate extends SensorEntity{

   private Double meanAccelerate;

    public Accelerate(Float accX, Float accY, Float accZ) {
       meanAccelerate = Math.sqrt(Math.pow(accX,2)+Math.pow(accY,2)+Math.pow(accZ,2));
    }

    public Double getMeanAccelerate() {
        return meanAccelerate;
    }

    public void setMeanAccelerate(Double meanAccelerate) {
        this.meanAccelerate = meanAccelerate;
    }
}
