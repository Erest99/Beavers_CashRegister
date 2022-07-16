package com.example.pokladna.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.BuySection.Buy;
import com.example.pokladna.MainActivity;
import com.example.pokladna.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Profiles extends AppCompatActivity {

    Spinner spinner;
    Button newProfile, changePassword, deleteProfile;


    ArrayList<String> profiles = new ArrayList<>();
    ArrayList<String> passwords = new ArrayList<>();

    ArrayList<String> profilesS = new ArrayList<>();
    ArrayList<String> passwordsS = new ArrayList<>();
    final String PROFILES = "profiles";
    final String PASSWORDS = "passwords";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiles);
        spinner = findViewById(R.id.spinner2);
        changePassword = findViewById(R.id.changePassword);
        deleteProfile =  findViewById(R.id.deleteProfile);
        newProfile = findViewById(R.id.newProfile);

        //load profiles from SP
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        profiles = new ArrayList<>(Arrays.asList(sharedPref.getString(PROFILES,"admin").split(",")));
        passwords = new ArrayList<>(Arrays.asList(sharedPref.getString(PASSWORDS,"0729").split(",")));

        profilesS = new ArrayList<>(profiles);
        passwordsS = new ArrayList<>(passwords);
        profilesS.remove(0);
        passwordsS.remove(0);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Profiles.this,android.R.layout.simple_spinner_item,profilesS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        newProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // OPEN add profile activity
                Intent intent = new Intent(Profiles.this,AddProfile.class);
                startActivity(intent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show input dialog to insert new password for selected profile
                inputDialog();


            }
        });

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete selected profile
                profiles.remove(spinner.getSelectedItemPosition()+1);
                passwords.remove(spinner.getSelectedItemPosition()+1);
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
                Toast.makeText(Profiles.this, "Profil odebrán", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Profiles.this,Profiles.class);
                startActivity(intent);
            }
        });


    }


    void inputDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(profiles.get(spinner.getSelectedItemPosition()+1));
        builder.setMessage(getApplicationContext().getResources().getString(R.string.newPassword));
        final EditText passwordInput = new EditText(Profiles.this);
        passwordInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(passwordInput);
        builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.activity_add_buy_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                passwords.set(spinner.getSelectedItemPosition()+1,passwordInput.getText().toString().trim());


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
                Toast.makeText(Profiles.this, "Heslo změněno", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Profiles.this,Profiles.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profiles.this, Admin.class);
        startActivity(intent);
    }
}