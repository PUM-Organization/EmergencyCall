package com.pomorganization.emergencycall;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.lang.Math;


public class MainActivity extends ActionBarActivity implements SensorEventListener {




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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        textViewAccX.setText("X: "+Float.toString(x));
        textViewAccY.setText("Y: "+Float.toString(y));
        textViewAccZ.setText("Z: "+Float.toString(z));
        textViewAccQ.setText("Q: "+Double.toString(q));

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

            q = Math.sqrt(Math.pow(x,2)+Math.pow(y,2)+Math.pow(z,2));

            if (q < 2){
                alertOn = true;
            }

            if (q > 8 && alertOn == true){
                alert();
                alertOn = false;
            }


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
        boolean isSensorListenerInitialized = sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_FASTEST);

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
    public void onStopButtonClickListener(View view) throws IOException {

        //TODO code there

    }

    public void alert (){
        Toast.makeText(getApplicationContext(), "ALERT!", Toast.LENGTH_SHORT).show();
    }


    //
}