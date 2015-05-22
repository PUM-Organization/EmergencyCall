package com.pomorganization.helpers;

import com.pomorganization.Models.SensorsData;

/**
 * Created by Daniel on 5/19/2015.
 */
public class DataSingleton {

    public static final int SHIFT_REGISTER_SIZE = 250;
    private static DataSingleton ourInstance;

    public synchronized static DataSingleton getInstance() {
        if(ourInstance==null)
        {
            ourInstance = new DataSingleton();
        }
        return ourInstance;
    }

    private DataSingleton() {
        sensorsData = new ShiftRegisterList<>(SHIFT_REGISTER_SIZE);
    }

    public  ShiftRegisterList<SensorsData> sensorsData;


}
