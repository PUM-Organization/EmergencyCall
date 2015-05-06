package com.pomorganization.emergencycall;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;


public class AlarmActivity extends ActionBarActivity {

    private boolean isFakeAlarm = false;
    private TextView timerValue;

    private TimerTask mTimerTask;
    private Timer timer = new Timer();
    private Handler handler = new Handler();

    private int counter =20;//20 seconds to turn off alarm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        timerValue = (TextView)findViewById(R.id.time);
        startTimer();
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

    //Cancel Alarm
    public void onClick(View view)
    {
        isFakeAlarm = true;
        if(mTimerTask!=null)
        {
            mTimerTask.cancel();
        }
        startActivity(new Intent(this,MainActivity.class));

    }


    public void startTimer(){
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(counter==0 && !isFakeAlarm)
                        {
                            //TODO : send sms , call , publish alarm on  fbl
                        }
                        else{
                            counter--;
                            timerValue.setText(counter + " s");
                        }

                    }
                });
            }
        };
        timer.schedule(mTimerTask,0,1000);
    }

}
