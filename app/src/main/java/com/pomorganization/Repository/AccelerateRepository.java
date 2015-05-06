package com.pomorganization.Repository;

import android.database.sqlite.SQLiteDatabase;

import com.pomorganization.Models.Accelerate;

import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 5/6/2015.
 * Repository with Accelerations Data
 */
public class AccelerateRepository {
    private SQLiteDatabase db;

    public AccelerateRepository(SQLiteDatabase db) {
        this.db = db;
    }



    public Accelerate addAccelerate(Accelerate accelerate)
    {
        return null;
    }
    public Accelerate getAccelerateById(long id)
    {
        return null;
    }
    public List<Accelerate> getAccelerateFromTime(Date start,Date end )
    {
        return null;
    }
    public List<Accelerate> getAllAccelerate()
    {
        return null;
    }
    public void removeAccelerate()
    {

    }
}
