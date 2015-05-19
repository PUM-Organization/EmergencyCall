package com.pomorganization.emergencycall;

import android.app.Notification;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pomorganization.Models.SensorsData;
import com.pomorganization.helpers.ShiftRegisterList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.lang.Math;
import java.util.Timer;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private static final int SHIFT_REGISTER_SIZE = 500;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private FileOutputStream fos;
    private TextView listFiles;
    private String textToSave ="";
    float x = 0;
    float y = 0;
    float z = 0;
    double q = 10;
    boolean alertOn = false;
    boolean alertSoundOn = false;
    Notification notification = new Notification();
    int counter = 0;

    private DataSingleton dataSingleton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        dataSingleton = DataSingleton.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//daniel
        listFiles = (TextView) findViewById(R.id.fileList);
        listFiles.setText("");
        for(String file : fileList())
        {
            listFiles.setText(file+"     ");
        }

        TextView textViewAccX = (TextView) findViewById(R.id.acc_x);
        TextView textViewAccY = (TextView) findViewById(R.id.acc_y);
        TextView textViewAccZ = (TextView) findViewById(R.id.acc_z);
        TextView textViewAccQ = (TextView) findViewById(R.id.acc_q);
        TextView textViewC = (TextView) findViewById(R.id.C);

        textViewAccX.setText("X: "+Float.toString(x));
        textViewAccY.setText("Y: "+Float.toString(y));
        textViewAccZ.setText("Z: "+Float.toString(z));
        textViewAccQ.setText("Q: "+Double.toString(q));
        textViewC.setText("Counter: "+Integer.toString(counter));


        Timer timer1 = new Timer();
        Checking timer1_task = new Checking();   //przesylamy do timera 500 probek danych z akcelerometru przy f probkowania akcelerometru=100Hz to dane z ostatnich 5s
        timer1.schedule(timer1_task, 5000, 5000);

///

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
    //daniel
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            x= event.values[0];
            y= event.values[1];
            z= event.values[2];

            dataSingleton.sensorsData.add(new SensorsData(x,y,z,null));

        }
        TextView textViewAccX = (TextView) findViewById(R.id.acc_x);
        TextView textViewAccY = (TextView) findViewById(R.id.acc_y);
        TextView textViewAccZ = (TextView) findViewById(R.id.acc_z);
        TextView textViewAccQ = (TextView) findViewById(R.id.acc_q);

        textViewAccX.setText("X: "+Float.toString(x));
        textViewAccY.setText("Y: "+Float.toString(y));
        textViewAccZ.setText("Z: "+Float.toString(z));
        textViewAccQ.setText("Q: "+Double.toString(q));
        textToSave =new Date().toString() + ","+ Float.toString(x) + ","+Float.toString(y) + ","+Float.toString(z) +"\n";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    public void onStartButtonClickListener(View view) throws IOException {

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        Toast.makeText(getApplicationContext(),"Inicjalizacja ...", Toast.LENGTH_SHORT).show();
        boolean isSensorListenerInitialized = sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);

        if(isSensorListenerInitialized)
        {
            Toast.makeText(getApplicationContext(),"Inicjalizacja zako?czona",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"B??d",Toast.LENGTH_SHORT).show();
        }

        TextView textViewAccX = (TextView) findViewById(R.id.acc_x);
        TextView textViewAccY = (TextView) findViewById(R.id.acc_y);
        TextView textViewAccZ = (TextView) findViewById(R.id.acc_z);
        TextView textViewAccQ = (TextView) findViewById(R.id.acc_q);

        textViewAccX.setText("X: "+Float.toString(x));
        textViewAccY.setText("Y: "+Float.toString(y));
        textViewAccZ.setText("Z: "+Float.toString(z));
        textViewAccQ.setText("Q: "+Double.toString(q));

    }
    public void onStopButtonClickListener(View view) throws IOException{
        Timer timer1 = new Timer();
        Checking timer1_task = new Checking();   //przesy?amy do timera 500 probek danych z akcelerometru przy f probkowania akcelerometru=100Hz to dane z ostatnich 5s
        timer1.schedule(timer1_task, 2000, 2000);
    }

    public void alert (){
        Toast.makeText(getApplicationContext(), "ALERT!", Toast.LENGTH_SHORT).show();
        alertSound(alertSoundOn = true);
    }

    public void alertSound(boolean alertSoundOn){
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sonsongsong);

        if (alertSoundOn == true){
            mp.setVolume(0.1F, 0.1F);
            mp.start();
            counter++;
            TextView textViewC = (TextView) findViewById(R.id.C);
            textViewC.setText("Counter: " + Integer.toString(counter));
        } else if (alertSoundOn == false) {
            mp.stop();
        }
    }

    //
}