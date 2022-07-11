package com.example.pokladna.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pokladna.BuySection.Buy;
import com.example.pokladna.MainActivity;
import com.example.pokladna.R;

public class Login extends AppCompatActivity {

    Spinner spinner;
    EditText password;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        spinner = findViewById(R.id.spinner);
        password = findViewById(R.id.pw);
        confirm =  findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String realpw =  getResources().getStringArray(R.array.Passwords)[spinner.getSelectedItemPosition()];
                if(realpw.equals(password.getText().toString().trim())) {
                    //Toast.makeText(Login.this, realpw, Toast.LENGTH_SHORT).show();
                    String profile = "not selected";
                    if(spinner.getSelectedItemPosition()==0)profile = "admin";
                    else if(spinner.getSelectedItemPosition()==1)profile = "Atym";
                    else if(spinner.getSelectedItemPosition()==2)profile = "Btym";
                    SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("profile", profile);
                    editor.apply();
                    Toast.makeText(Login.this, profile, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Login.this, "špatné heslo", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}