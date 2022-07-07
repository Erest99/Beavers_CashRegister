package com.example.pokladna.SellSection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokladna.BuySection.Buy;
import com.example.pokladna.Item;
import com.example.pokladna.MainActivity;
import com.example.pokladna.MyDatabaseHelper;
import com.example.pokladna.R;

import java.util.ArrayList;
import java.util.List;

public class Sell extends AppCompatActivity {

    RecyclerView recyclerView;

    Button sellButton, debtButton;
    MyDatabaseHelper myDB;
    List<Item> data;
    CustomAdapter customAdapter;
    List<Item> cart;

    ImageView empty_image;
    TextView no_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_layout);

        recyclerView = findViewById(R.id.recyclerView);
        empty_image = findViewById(R.id.imageViewNoDataSS);
        no_data = findViewById(R.id.textViewNoDataSS);
        sellButton = findViewById(R.id.sellButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cart contains selected items
                cart = customAdapter.getCart();
            }
        });
        debtButton = findViewById(R.id.debtButton);
        debtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO read cart, create debt record with name input in dialog and todays date
            }
        });

        myDB = new MyDatabaseHelper(Sell.this);
        data = new ArrayList<>();

        data = storeDataInList();

        customAdapter = new CustomAdapter(Sell.this, Sell.this,data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Sell.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            recreate();
        }

    }

    List<Item> storeDataInList()
    {
        List<Item> items = new ArrayList<Item>();
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0)
        {
            Log.w("Data display", "no data to display");
            empty_image.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);

        }
        else
        {
            empty_image.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);

            while(cursor.moveToNext())
            {
                Item item = new Item(Long.valueOf(cursor.getInt(0)),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4));
                items.add(item);
            }
        }

        return items;
    }


    void confirmDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getApplicationContext().getResources().getString(R.string.deleteAll));
        builder.setMessage(getApplicationContext().getResources().getString(R.string.deleteAlllonger));
        builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(Sell.this);
                myDB.deleteAllData();
                Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.deleting),Toast.LENGTH_SHORT).show();
                recreate();
            }
        });
        builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
