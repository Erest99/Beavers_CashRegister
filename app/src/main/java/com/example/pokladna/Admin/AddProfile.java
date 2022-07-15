package com.example.pokladna.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class AddProfile extends AppCompatActivity {

    EditText userName, passWord;
    Button confirm;

    ArrayList<String> profiles = new ArrayList<>();
    ArrayList<String> passwords = new ArrayList<>();
    final String PROFILES = "profiles";
    final String PASSWORDS = "passwords";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_profile);

        userName = findViewById(R.id.profileName);
        passWord = findViewById(R.id.profilePassword);
        confirm = findViewById(R.id.addProfile);

        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        profiles = new ArrayList<>(Arrays.asList(sharedPref.getString(PROFILES,"admin").split(",")));
        passwords = new ArrayList<>(Arrays.asList(sharedPref.getString(PASSWORDS,"0729").split(",")));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profiles.add(userName.getText().toString().toLowerCase(Locale.ROOT).trim());
                passwords.add(passWord.getText().toString().toLowerCase(Locale.ROOT).trim());

                StringBuilder sbu = new StringBuilder();
                StringBuilder sbp = new StringBuilder();
                for (int i = 0; i < profiles.size(); i++) {
                    sbu.append(profiles.get(i)).append(",");
                    sbp.append(passwords.get(i)).append(",");
                }
                SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(PROFILES, sbu.toString());
                editor.putString(PASSWORDS,sbp.toString());
                editor.apply();
                Toast.makeText(AddProfile.this, "Nový profil přidán", Toast.LENGTH_SHORT).show();
            }
        });
    }
}