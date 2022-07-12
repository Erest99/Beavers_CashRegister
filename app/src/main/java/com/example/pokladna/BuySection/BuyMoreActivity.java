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

    String name,id, amount, buy, sell;

    int money;
    String tax;

    String admin = "admin";
    String acko = "Atym";
    String bcko = "Btym";
    String[] profiles = {"penizeAdmin","penizeAtym","penizeB"};
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
                    SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
                    String profile = sharedPref.getString("profile", "admin");
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
        if(profile.equals(admin))activeProfile = 0;
        else if(profile.equals(acko))activeProfile = 1;
        else if(profile.equals(bcko))activeProfile = 2;

        sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        money = sharedPref.getInt(profiles[activeProfile], 0);


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

            nameInput.setText(name);
            amountInput.setText(amount);
            buyInput.setText(buy);
            sellInput.setText(sell);
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
        editor.putInt(profiles[activeProfile], money);
        editor.apply();
    }

}