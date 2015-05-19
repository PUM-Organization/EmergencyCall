package com.pomorganization.emergencycall;

import android.content.Context;
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


/**
 * Created by ASUSPRO on 2015-05-19.
 metoda run po wywo?aniu jej z Main Activity wykonuje si? z zadanym okresem, sktypt w niej zawarty pokazuje, ?e to dzia?a

 */
public class Checking extends TimerTask{

    private static final Long MINIMAL_TIME_OF_FALL = 10L;
    private DataSingleton dataSingleton;
    Long beginOfFallTimeStamp = 0L;
    Long endOfFallTimeStamp = 0L;
    public Checking(){
        dataSingleton = DataSingleton.getInstance();
    }

    @Override
    public void run() {
        for(int x=0; x<dataSingleton.sensorsData.size();++x)
        {
            SensorsData data = dataSingleton.sensorsData.get(x);
            if(data.getAccMediumValue()<2)
            {
                if(beginOfFallTimeStamp ==0)
                    beginOfFallTimeStamp = data.getTimeStamp();
                else
                    endOfFallTimeStamp = data.getTimeStamp();
            }
            else
            {
                Long timeOfFall = endOfFallTimeStamp - beginOfFallTimeStamp;
                if(timeOfFall>MINIMAL_TIME_OF_FALL)
                {
                    alarm();
                }
                else {
                    beginOfFallTimeStamp =0L;
                    endOfFallTimeStamp = 0L;
                }
            }


        }

    }

    public void alarm()
    {
        Log.d("PUM2", "ALARM!!!!!!!!!!!!!!");
    }



}
