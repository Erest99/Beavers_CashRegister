package com.example.pokladna.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.R;

public class Profiles extends AppCompatActivity {

    Spinner spinner;
    Button newProfile, changePassword, deleteProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiles);
        spinner = findViewById(R.id.spinner2);
        changePassword = findViewById(R.id.changePassword);
        deleteProfile =  findViewById(R.id.deleteProfile);
        newProfile = findViewById(R.id.newProfile);

        newProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // OPEN add profile activity
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show input dialog to insert new password for selected profile

            }
        });

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete selected profile

            }
        });


    }
}