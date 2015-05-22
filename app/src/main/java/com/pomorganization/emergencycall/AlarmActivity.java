package com.pomorganization.emergencycall;

import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class AlarmActivity extends ActionBarActivity {

    private boolean ALARM_STARTED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

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

    }

    public void alert (){
        Toast.makeText(getApplicationContext(), "ALERT!", Toast.LENGTH_SHORT).show();
        alertSound(ALARM_STARTED = true);
    }
    public void alertSound(boolean alertSoundOn){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sonsongsong);

        if (alertSoundOn == true){
            mp.setVolume(0.1F, 0.1F);
            mp.start();


        } else if (alertSoundOn == false) {
            mp.stop();
        }
    }

}
