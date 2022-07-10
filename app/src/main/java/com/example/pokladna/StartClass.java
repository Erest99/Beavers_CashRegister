package com.example.pokladna;

import android.app.Application;
import android.util.Log;

public class StartClass extends Application {

    public StartClass() {
        // this method fires only once per application start.
        // getApplicationContext returns null here

        Log.i("main", "Constructor fired");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // this method fires once as well as constructor
        // but also application has context here

        Log.i("main", "onCreate fired");
    }
}
