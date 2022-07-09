package com.example.pokladna;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pokladna.BuySection.Buy;
import com.example.pokladna.Debts.Debts;
import com.example.pokladna.EditSection.Storage;
import com.example.pokladna.SellSection.Sell;

public class MainActivity extends AppCompatActivity {


    private int money;
    private Boolean loaded = false;

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }


    Button buyButton, storageButton, sellButton, debtButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buyButton = findViewById(R.id.buyButton);
        storageButton = findViewById(R.id.storageButton);
        sellButton = findViewById(R.id.sellButton);
        debtButton = findViewById(R.id.debtSectionButton);

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

        debtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Debts.class);
                startActivity(intent);
            }
        });

        if(!loaded) money = loadMoney();

    }

    private int loadMoney()
    {
        //TODO return saved money for profile
        loaded = true;
        return 0;
    }

}