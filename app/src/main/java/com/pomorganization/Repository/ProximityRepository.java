package com.pomorganization.Repository;

import android.database.sqlite.SQLiteDatabase;

import com.pomorganization.Models.Proximity;

import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 5/6/2015.
 * Repository with Proximity Data
 */
public class ProximityRepository {
    private SQLiteDatabase db;

    public ProximityRepository(SQLiteDatabase db) {
        this.db = db;
    }
    public Proximity addProximity(Proximity proximity)
    {
        return null;
    }
    public Proximity getProximityById(long id)
    {
        return null;
    }
    public List<Proximity> getProximityFromTime(Date start,Date end)
    {
        return null;
    }
    public List<Proximity> getAllProximity()
    {
        return null;
    }
    public void removeProximity(long id)
    {

    }
}
