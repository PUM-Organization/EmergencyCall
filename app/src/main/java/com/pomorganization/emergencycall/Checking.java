package com.pomorganization.emergencycall;

import android.content.Context;
import android.hardware.SensorEvent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pomorganization.Models.SensorsData;
import com.pomorganization.helpers.ShiftRegisterList;

import java.io.ObjectInputStream;
import java.util.Timer;
import java.util.TimerTask;
import android.hardware.SensorEvent;


/**
 * Created by ASUSPRO on 2015-05-19.
 metoda run po wywo?aniu jej z Main Activity wykonuje si? z zadanym okresem, sktypt w niej zawarty pokazuje, ?e to dzia?a

 */
public class Checking extends TimerTask{

    //minimal value of time falling down in ms
    private static final Long MINIMAL_TIME_OF_FALL = 10L;
    //singleto with sensors data
    private DataSingleton dataSingleton;


    //temp variable to calculate falling time ( endOfFallTimeStamp - beginOfFallTimeStamp)
    Long beginOfFallTimeStamp = 0L;
    Long endOfFallTimeStamp = 0L;
    SensorsData data;
    AccelerateSensor accelerateSensor = new AccelerateSensor();
    private int alertQualifi = 0;


    //constructor
    public Checking(){
        //get instance from singleton (if not exist make new)
        dataSingleton = DataSingleton.getInstance();
    }

    @Override
    public void run() {
        MainActivity.getAccelerateSensor(accelerateSensor);
        dataSingleton.sensorsData.add(new SensorsData(accelerateSensor.getX(), accelerateSensor.getY(), accelerateSensor.getZ(), null));

        for (int i = 0; i < dataSingleton.sensorsData.size(); i++){
            Log.d("Parametr SensorData: ", "Acc = " + dataSingleton.sensorsData.get(i).getAccMediumValue() + "    Time = " + dataSingleton.sensorsData.get(i).getTimeStamp());
        }

        for (int i = 0; i < dataSingleton.sensorsData.size(); i++){
            if (dataSingleton.sensorsData.get(i).getAccMediumValue() < 2){
                alertQualifi++;
            }
        }

        if (alertQualifi > 8){
            alertQualifi = 0;
            alarm();
        }



//        for(int x=0; x<dataSingleton.sensorsData.size();++x)
//        {
//            //current data
//            SensorsData data = dataSingleton.sensorsData.get(x);
//
//
//            if(data.getAccMediumValue()<2)
//            {
//                //if fall is begin
//                if(beginOfFallTimeStamp ==0)
//                    beginOfFallTimeStamp = data.getTimeStamp();
//                //if fall is next
//                else
//                    endOfFallTimeStamp = data.getTimeStamp();
//            }
//            //end of falling or fall isn't started
//            else
//            {
//                //calculate time of falling
//                Long timeOfFall = endOfFallTimeStamp - beginOfFallTimeStamp;
//                //check if time of falling is enough big
//                if(timeOfFall>MINIMAL_TIME_OF_FALL)
//                {
//                    //if yes call alarm
//                    alarm();
//                }
//                else {
//                    beginOfFallTimeStamp =0L;
//                    endOfFallTimeStamp = 0L;
//                }
//            }
//
//
//        }

    }

    public void alarm()
    {
        Log.d("PUM2", "ALARM!!!!!!!!!!!!!!");
    }



}
