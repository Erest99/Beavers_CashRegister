package com.example.pokladna.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.Admin.Admin;
import com.example.pokladna.MainActivity;
import com.example.pokladna.R;

public class Login extends AppCompatActivity {

    Spinner spinner;
    EditText password;
    Button confirm;

    String[] profiles;
    String[] passwords;
    String[] profilesMoney = {"cashAdmin","cash1","cash2","cash3","cash4","cash5","cash6","cash7","cash7","cash8","cash9"};
    String[] sessions = {"sessionAdmin","session1","session2","session3","session4","session5","session6","session7","session8","session9"};
    final String PROFILES = "profiles";
    final String PASSWORDS = "passwords";

    int activeProfile = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        spinner = findViewById(R.id.spinner);
        password = findViewById(R.id.pw);
        confirm =  findViewById(R.id.confirm);

        //load profiles from SP
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        profiles = sharedPref.getString(PROFILES,"admin").split(",");
        passwords = sharedPref.getString(PASSWORDS,"0729").split(",");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Login.this,android.R.layout.simple_spinner_item,profiles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String realpw =  passwords[spinner.getSelectedItemPosition()];
                if(realpw.equals(password.getText().toString().trim())) {
                    SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("profile", profiles[spinner.getSelectedItemPosition()]);
                    editor.apply();

                    Toast.makeText(Login.this, profiles[spinner.getSelectedItemPosition()], Toast.LENGTH_SHORT).show();
                    if(spinner.getSelectedItemPosition()==0)
                    {
                        Intent intent = new Intent(Login.this, Admin.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }

                }else{
                    Toast.makeText(Login.this, "špatné heslo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                password.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                password.setText("");
            }
        });



    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        password.setText("");
        //load profiles from SP
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        profiles = sharedPref.getString(PROFILES,"admin").split(",");
        passwords = sharedPref.getString(PASSWORDS,"0729").split(",");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Login.this,android.R.layout.simple_spinner_item,profiles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}