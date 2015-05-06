package com.pomorganization.Models;

/**
 * Created by Daniel on 5/6/2015.
 */
public abstract class Entity {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
