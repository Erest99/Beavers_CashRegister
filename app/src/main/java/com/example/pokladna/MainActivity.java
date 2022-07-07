package com.example.pokladna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pokladna.BuySection.Buy;
import com.example.pokladna.EditSection.Storage;
import com.example.pokladna.SellSection.Sell;

public class MainActivity extends AppCompatActivity {

    Button buyButton, storageButton, sellButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buyButton = findViewById(R.id.buyButton);
        storageButton = findViewById(R.id.storageButton);
        sellButton = findViewById(R.id.sellButton);

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Buy.class);
                startActivity(intent);
            }
        });

        storageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Storage.class);
                startActivity(intent);
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Sell.class);
                startActivity(intent);
            }
        });

    }
}