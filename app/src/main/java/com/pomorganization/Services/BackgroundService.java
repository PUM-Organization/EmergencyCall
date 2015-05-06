package com.pomorganization.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;

import com.pomorganization.Models.Accelerate;
import com.pomorganization.Models.Proximity;
import com.pomorganization.emergencycall.AlarmActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Daniel on 5/6/2015.
 */
public class BackgroundService extends Service  implements SensorEventListener{
    Context context = this;
    public static final long ACCELEROMETER_INTERVAL = 40; // 25Hz
    public static final long PROXIMITY_INTERVAL = 1000; //1Hz
    public static final long LOCATION_INTERVAL = 60 * 1000; // 1min
    private static final long DETECTION_INTERVAL = 5 * 1000; //0.2Hz (5s)


    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor proximity;

    private Timer mTimer = null;
    private Handler mHandler = new Handler() ;

    //temp
    private Accelerate acc;
    private Proximity prox;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);


        // cancel if already existed
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
       mTimer.scheduleAtFixedRate(new AccelerateSaveTask(),0,ACCELEROMETER_INTERVAL);
       mTimer.scheduleAtFixedRate(new ProximitySaveTask(),0,PROXIMITY_INTERVAL);
       mTimer.scheduleAtFixedRate(new LocationSaveTask(),0,LOCATION_INTERVAL);
       mTimer.scheduleAtFixedRate(new DetectFallTask(),0,DETECTION_INTERVAL);
    }


    //for
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            //TODO : get values
        }
        else if(sensor.getType()==Sensor.TYPE_PROXIMITY)
        {
            //TODO : get values
        }

    }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }


    class AccelerateSaveTask extends TimerTask
    {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //TODO : Save Accelerate to Database

                }
            });

        }
    }
    class ProximitySaveTask extends TimerTask
    {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //TODO : Save Proximity to Database
                }
            });

        }
    }
    class LocationSaveTask extends TimerTask
    {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //TODO : Save Location to Database
                }
            });
        }
    }

    class DetectFallTask extends TimerTask
    {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    //jesli wykryto upadek
                    if(detectFall(null,null))
                    {
                        Intent alarmActivityIntent = new Intent(context,AlarmActivity.class);
                        alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(alarmActivityIntent);
                        //TODO : Wywołaj alarm dźwiek, światło wibracje pokaż activity z możliwością anulowania alarmu
                    }

                }
            });
        }
    }

    //return true if fall is detected
    private boolean detectFall(List<Accelerate> accelerations, List<Proximity> proximities)
    {
        //TODO : detect fall
        return false;
    }

}


