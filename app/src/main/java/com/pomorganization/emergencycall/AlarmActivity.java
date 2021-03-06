package com.pomorganization.emergencycall;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.pomorganization.Models.RowBean;

import com.pomorganization.services.SharedPreference;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Alarm activity shows on fall detected
 */
public class AlarmActivity extends ActionBarActivity {

    private MediaPlayer mp;
    private int counter = 20;
    private TextView textView;
    private Handler timerHandler = new Handler();
    private Vibrator vibrator;
    private LocationManager locationManager;
    private List<RowBean> contacts;

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            counter--;
            textView.setText("Powiadomienie zostanie wyslane za: " + Integer.toString(counter));
            timerHandler.postDelayed(this, 1000);
            vibrator.vibrate(700);
            if (counter == 0){
                timerHandler.removeCallbacks(this);
                counter = 20;
                textView.setText("Wyslano powiadomienie o wypadku!");
                String locationProvider = LocationManager.NETWORK_PROVIDER;
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
                final String url = "http://pum.jrk.ovh/"+lastKnownLocation.getLatitude()+"/"+lastKnownLocation.getLongitude()+"/text";
                String adres = "";

                if(contacts !=null)
                {
                    for(RowBean contact : contacts)
                    {
                            alertSMS(contact.getPhoneNumber(), adres + "http://pum.jrk.ovh/"+lastKnownLocation.getLatitude()+"/"+lastKnownLocation.getLongitude());
                    }
                }

                alertFacebook("szerokosc : "+lastKnownLocation.getLatitude()+", dlugosc : "+lastKnownLocation.getLongitude());
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        FacebookSdk.sdkInitialize(getApplicationContext());
        SharedPreference sharedPreference = new SharedPreference(getApplicationContext());
        contacts = sharedPreference.loadContacts();
        textView =(TextView) findViewById(R.id.textView);
        mp = MediaPlayer.create(getApplicationContext(), R.raw.sonsongsong);
        timerHandler.postDelayed(timerRunnable,0);
        textView.setText(Integer.toString(counter));
        alert();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
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
        mp.stop();
        timerHandler.removeCallbacks(timerRunnable);
        counter = 0;
        textView.setText("Powiadomienie zostalo anulowane.");
        Toast.makeText(this,"Powiadomienie zostalo anulowane.",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void alert() {
        Toast.makeText(getApplicationContext(), "ALERT!", Toast.LENGTH_SHORT).show();
        alertSound();
    }

    private void alertFacebook(String location) {

        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logInWithPublishPermissions(this, Arrays.asList("publish_actions"));

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newPostRequest(AccessToken.getCurrentAccessToken(),
                "/me/feed/", null, callback);
        Bundle bundle = request.getParameters();
        bundle.putString("message","Potrzebuje pomocy, jestem tutaj : "+location);

        request.setParameters(bundle);
        request.executeAsync();

    }

    private void alertSMS(String phoneNo,String location) {
        String message = "Potrzebuje pomocy, upadlem :( jestem tutaj : " + location;

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo,null,message,null,null);

    }

    public void alertSound(){
        mp.setVolume(1F, 1F);
        mp.start();
    }


    GraphRequest.Callback callback = new GraphRequest.Callback() {
        @Override
        public void onCompleted(GraphResponse graphResponse) {

        }
    };



}
