package com.example.pokladna;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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

        SharedPreferences sharedPref = this.getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        boolean init = sharedPref.getBoolean("init",false);


        Log.i("main", "onCreate fired");
        if(!init) {
            //save profiles to S.P.
            final String PROFILES = "profiles";
            final String PASSWORDS = "passwords";

            String[] profiles = {"admin"};
            String[] passwords = {"0729"};

            StringBuilder sbu = new StringBuilder();
            StringBuilder sbp = new StringBuilder();
            for (int i = 0; i < profiles.length; i++) {
                sbu.append(profiles[i]).append(",");
                sbp.append(passwords[i]).append(",");
            }
            sharedPref = this.getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(PROFILES, sbu.toString());
            editor.putString(PASSWORDS, sbp.toString());
            editor.putBoolean("init",true);
            editor.apply();
        }


        final String ORDER = "order";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10.; i++) {
            sb.append(String.valueOf(i)).append(",");
        }
        sharedPref = this.getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(ORDER, sb.toString());
        editor.apply();
    }
}
