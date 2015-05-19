package com.pomorganization.emergencycall;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ASUSPRO on 2015-05-19.
 metoda run po wywo?aniu jej z Main Activity wykonuje si? z zadanym okresem, sktypt w niej zawarty pokazuje, ?e to dzia?a

 */
public class Checking extends TimerTask{
    int licznik = 2;
    int i=0;

    public Checking(int licznik){
        this.licznik = licznik;
    }

    @Override
    public void run() {
        i++;

        if (i == 5){
            System.exit(0);
        }

    }



}
