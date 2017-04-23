package com.hackfresno17.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class Service extends IntentService {

    private static final String TAG = "com.hackfresno17.myapp";

    public Service() {
        super("Service");

    }

    protected void onHandleIntent(Intent intent){
        Log.i(TAG, "Service is now started!");
    }
}
