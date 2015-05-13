package com.pomorganization.emergencycall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.facebook.FacebookSdk;


import com.pomorganization.Services.SMSService;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onStartServiceButtonClick(View view)
    {

        Toast toast = new Toast(this);
        toast.setText("Background Service Started");
        toast.show();
    }
    public void onConfigureButtonClick(View view)
    {
        startActivity(new Intent(this, ConfigureActivity.class));
    }
    public void onSendSMSButtonClick(View view)
    {
        SMSService smsService = new SMSService();
        try
        {
            smsService.SendSMS("+48889075102","ALARM");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
}
