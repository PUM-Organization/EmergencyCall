package com.pomorganization.helpers;

import com.pomorganization.Models.SensorsData;

/**
 * Created by Daniel on 5/19/2015.
 * Singleton for keep one instance of sensors data and shares object beetwen many classes
 */
public class DataSingleton {

    /**
     * size of buffer for data from sensors
     */
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

    /**
     * data from sensors on list
     */
    public  ShiftRegisterList<SensorsData> sensorsData;


}
