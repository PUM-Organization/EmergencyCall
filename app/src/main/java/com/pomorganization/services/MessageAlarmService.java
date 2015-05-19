package com.pomorganization.services;

import android.telephony.SmsManager;

/**
 * Created by Daniel on 5/19/2015.
 */
public class MessageAlarmService  {

    public void sendSms(String phoneNo,String location)
    {
        String message = "Potrzebuj? pomocy, upad?em :( jestem tutaj : " + location;

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo,null,message,null,null);
    }

    //TODO : implement facebook api
    public void publishOnFacebook()
    {

    }
}
