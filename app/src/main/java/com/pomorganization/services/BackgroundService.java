package com.pomorganization.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.pomorganization.Models.SensorsData;
import com.pomorganization.emergencycall.AlarmActivity;
import com.pomorganization.helpers.DataSingleton;
import com.pomorganization.helpers.ShiftRegisterList;

/**
 * Created by Daniel on 5/19/2015.
 * Main Service which is running in  background
 */

public class BackgroundService extends Service implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    private DataSingleton dataSingleton;
    private Handler handler;

    private float x = 0;
    private float y = 0;
    private float z = 0;


    //minimal value of time falling down in ms
    private static final Long MINIMAL_TIME_OF_FALL = 190L;
    //temp variable to calculate falling time ( endOfFallTimeStamp - beginOfFallTimeStamp)
    private Long beginOfFallTimeStamp = 0L;
    private Long endOfFallTimeStamp = 0L;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate()
    {
        dataSingleton = DataSingleton.getInstance();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        FacebookSdk.sdkInitialize(getApplicationContext());

        handler = new Handler();
        handler.postDelayed(runnable, 5000);

        Toast.makeText(this,"Service started",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy()
    {
        Toast.makeText(this,"Service stopped",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            x= event.values[0];
            y= event.values[1];
            z= event.values[2];

            //add sensor data to FIFO Queue
            dataSingleton.sensorsData.add(new SensorsData(x, y, z, null));

        }


    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }



    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            check();
            handler.postDelayed(runnable,5000);
        }
    };


    /**
     * method to detect falling
     */
    public void check() {
        for (int x = 0; x < dataSingleton.sensorsData.size(); ++x) {
            //current data
            SensorsData data = dataSingleton.sensorsData.get(x);

            Log.d("PUM DATA ", data.toString());
            if (data.getAccMediumValue() < 5) {
                //if fall is begin
                if (beginOfFallTimeStamp == 0) {
                    beginOfFallTimeStamp = data.getTimeStamp();

                }

                //if fall is next
                else
                    endOfFallTimeStamp = data.getTimeStamp();

            }
            //end of falling or fall isn't started
            else {
                //calculate time of falling
                Long timeOfFall = endOfFallTimeStamp - beginOfFallTimeStamp;
                //check if time of falling is enough big
                if (timeOfFall > MINIMAL_TIME_OF_FALL) {

                    //if yes call alarm

                    timeOfFall = 0L;
                    beginOfFallTimeStamp = 0L;
                    endOfFallTimeStamp = 0L;
                    dataSingleton.sensorsData = new ShiftRegisterList<>(DataSingleton.SHIFT_REGISTER_SIZE);
                    Intent intent = new Intent(this,AlarmActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);


                } else {
                    beginOfFallTimeStamp = 0L;
                    endOfFallTimeStamp = 0L;
                }
            }


        }

    }


}
