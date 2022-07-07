package com.example.pokladna.BuySection;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pokladna.MyDatabaseHelper;
import com.example.pokladna.R;

public class BuyMoreActivity extends AppCompatActivity {

    EditText nameInput, amountInput,buyInput,sellInput, additionalInput;
    Button confirmButton;

    String name,id, amount, buy, sell;

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


                myDB.updateData(id,name,amount,buy,sell,getApplicationContext());
                Intent intent = new Intent(BuyMoreActivity.this, Buy.class);
                startActivity(intent);


            }
        });


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

}