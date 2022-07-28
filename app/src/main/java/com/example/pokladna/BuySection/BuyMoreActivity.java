package com.example.pokladna.BuySection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.DBStorage.MyDatabaseHelper;
import com.example.pokladna.R;

public class BuyMoreActivity extends AppCompatActivity {

    EditText nameInput, amountInput,buyInput,sellInput, additionalInput;
    Button confirmButton;

    String name,id, amount, buy, sell, profile;

    int money;
    String tax;

    String[] profiles;
    String[] profilesMoney = {"cashAdmin","cash1","cash2","cash3","cash4","cash5","cash6","cash7","cash7","cash8","cash9"};
    final String PROFILES = "profiles";

    int activeProfile = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_more);
        MyDatabaseHelper myDB = new MyDatabaseHelper(BuyMoreActivity.this);


        nameInput = findViewById(R.id.editTextNameUpdate);
        amountInput = findViewById(R.id.editTextAmmountUpdate);
        buyInput = findViewById(R.id.editTextBuyUpdate);
        sellInput = findViewById(R.id.editTextSellUpdate);
        confirmButton = findViewById(R.id.updateButton);
        additionalInput = findViewById(R.id.addInput);

        //prepare data
        getAndSetIntentData();

        //set action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(name);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString().trim();
                amount = String.valueOf(Integer.valueOf(amountInput.getText().toString().trim()) + Integer.valueOf(additionalInput.getText().toString().trim()));
                buy = buyInput.getText().toString().trim();
                sell = sellInput.getText().toString().trim();

                Context context = getApplicationContext();
                if( TextUtils.isDigitsOnly(buyInput.getText())
                        &&TextUtils.isDigitsOnly(sellInput.getText())
                        && TextUtils.isDigitsOnly(amountInput.getText())
                        &&nameInput.getText().toString().length()>0
                        &&buyInput.getText().toString().length()>0
                        &&sellInput.getText().toString().length()>0
                        &&amountInput.getText().toString().length()>0)
                {
                    myDB.updateData(id,name,amount,buy,sell,tax,profile,getApplicationContext());
                    money -= Integer.valueOf(additionalInput.getText().toString().trim()) * Integer.valueOf(buy);
                    Intent intent = new Intent(BuyMoreActivity.this, Buy.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context,context.getResources().getString(R.string.wrongInput), Toast.LENGTH_SHORT).show();
                }





            }
        });
        //set profile
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        String profile = sharedPref.getString("profile", "admin");
        profiles = sharedPref.getString(PROFILES,"admin").split(",");
        if(profile.equals(profiles[0]))activeProfile = 0;
        else if(profile.equals(profiles[1]))activeProfile = 1;
        else if(profile.equals(profiles[2]))activeProfile = 2;
        else if(profile.equals(profiles[3]))activeProfile = 3;
        else if(profile.equals(profiles[4]))activeProfile = 4;
        else if(profile.equals(profiles[5]))activeProfile = 5;
        else if(profile.equals(profiles[6]))activeProfile = 6;
        else if(profile.equals(profiles[7]))activeProfile = 7;
        else if(profile.equals(profiles[8]))activeProfile = 8;
        else if(profile.equals(profiles[9]))activeProfile = 9;

        sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        money = sharedPref.getInt(profilesMoney[activeProfile], 0);


    }

    void getAndSetIntentData()
    {
        if(getIntent().hasExtra("id")&&getIntent().hasExtra("name")&&getIntent().hasExtra("amount")&&getIntent().hasExtra("buy")&&
                getIntent().hasExtra("sell"))
        {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            amount = getIntent().getStringExtra("amount");
            buy = getIntent().getStringExtra("buy");
            sell = getIntent().getStringExtra("sell");
            tax = getIntent().getStringExtra("tax");
            profile = getIntent().getStringExtra("profile");

            nameInput.setText(name);
            amountInput.setText(amount);
            buyInput.setText(buy);
            sellInput.setText(sell);

            if(profile.equals("admin"))
            {
                additionalInput.setEnabled(false);
                confirmButton.setEnabled(false);
            }
        }
        else
        {
            Log.e("getIntentData","Wrong data");
        }
    }

    protected void onPause(){
        super.onPause();
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(profilesMoney[activeProfile], money);
        editor.apply();
    }

}