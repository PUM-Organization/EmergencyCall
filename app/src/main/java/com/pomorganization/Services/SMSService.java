package com.pomorganization.Services;


import android.telephony.SmsManager;

/**
 * Created by Daniel on 5/6/2015.
 */
public class SMSService {

    private SmsManager smsManager;

    public SMSService() {
        this.smsManager = SmsManager.getDefault();
    }

    public void SendSMS(String phoneNo,String Message)
    {
        smsManager.sendTextMessage(phoneNo,null,Message,null,null);
    }


}
