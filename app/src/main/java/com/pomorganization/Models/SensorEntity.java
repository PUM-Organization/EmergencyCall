package com.pomorganization.Models;

import java.util.Date;

/**
 * Created by Daniel on 5/6/2015.
 */
public abstract class SensorEntity extends Entity {

    private Date time;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
