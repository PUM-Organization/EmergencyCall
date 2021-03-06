package com.pomorganization.emergencycall;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by Daniel on 5/25/2015.
 * Maked for temp to generate signature for facebook
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        printHash();
    }

    /**
     * method to generate hash for facebook api
     */
    public void printHash()
    {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.pomorganization.emergencycall",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("DANIELK", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
