package com.example.pokladna.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.Admin.adminDebts.Debts;
import com.example.pokladna.Admin.adminStorage.Storage;
import com.example.pokladna.R;
import com.example.pokladna.ui.Login;

public class Admin extends AppCompatActivity {

    Button profileButton, storageButton, debtsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this,Profiles.class);
                startActivity(intent);

            }
        });

        storageButton = findViewById(R.id.storagesButton);
        storageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this, Storage.class);
                startActivity(intent);

            }
        });

        debtsButton = findViewById(R.id.debtsButton);
        debtsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this, Debts.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Admin.this, Login.class);
        startActivity(intent);
    }
}