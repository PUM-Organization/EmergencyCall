package com.pomorganization.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Daniel on 5/19/2015.
 */
//TODO : move detecting fall to background service from MainActivity
public class BackgroundService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
