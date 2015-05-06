package com.pomorganization.Repository;

import android.database.sqlite.SQLiteDatabase;

import com.pomorganization.Models.Location;
import com.pomorganization.Models.Proximity;

import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 5/6/2015.
 * Repository with Location Data
 */
public class LocationRepository {
    private SQLiteDatabase db;

    public LocationRepository(SQLiteDatabase db) {
        this.db = db;
    }
    public Proximity addLocation(Location location)
    {
        return null;
    }
    public Proximity getLocationById(long id)
    {
        return null;
    }
    public List<Proximity> getLocationsFromTime(Date start,Date end)
    {
        return null;
    }
    public List<Proximity> getAllLocations()
    {
        return null;
    }
    public void removeLocation(long id)
    {

    }
}
