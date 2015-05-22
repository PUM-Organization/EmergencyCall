package com.pomorganization.emergencycall;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.util.logging.LogRecord;


public class AlarmActivity extends ActionBarActivity {

    private boolean ALARM_STARTED;
    private TextView textView;
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        mp = MediaPlayer.create(getApplicationContext(), R.raw.sonsongsong);
        alert();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
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

    public void onCancelButtonClick(View view)
    {
        ALARM_STARTED = false;

        mp.pause();
            mp.stop();



    }

    public void alert (){
        Toast.makeText(getApplicationContext(), "ALERT!", Toast.LENGTH_SHORT).show();
        ALARM_STARTED = true;
        alertSound();
    }
    public void alertSound(){
        mp.setVolume(0.1F, 0.1F);
        mp.start();
    }


}
