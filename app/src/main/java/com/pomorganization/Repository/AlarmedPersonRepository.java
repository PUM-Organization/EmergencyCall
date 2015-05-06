package com.pomorganization.Repository;

import android.database.sqlite.SQLiteDatabase;
import com.pomorganization.Models.Proximity;


import java.util.List;

/**
 * Created by Daniel on 5/6/2015.
 * Repository with Persons to Alarm them
 */
public class AlarmedPersonRepository {
    private SQLiteDatabase db;

    public AlarmedPersonRepository(SQLiteDatabase db) {
        this.db = db;
    }

    public Proximity addPerson(Proximity proximity)
    {
        return null;
    }
    public Proximity getPersonById(long id)
    {
        return null;
    }

    public List<Proximity> getAllPersons()
    {
        return null;
    }
    public void removePerson(long id)
    {

    }
}
