package com.example.pokladna.BuySection;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokladna.Item;
import com.example.pokladna.MyDatabaseHelper;
import com.example.pokladna.R;

import java.util.ArrayList;
import java.util.List;

public class Buy extends AppCompatActivity {

    RecyclerView recyclerView;
    Button confirmButton;

    MyDatabaseHelper myDB;
    List<Item> data;
    CustomAdapter customAdapter;

    ImageView empty_image;
    TextView no_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_layout);

        recyclerView = findViewById(R.id.recyclerView);
        empty_image = findViewById(R.id.imageViewNoDataSS);
        no_data = findViewById(R.id.textViewNoDataSS);
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Buy.this, BuyToStorage.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(Buy.this);
        data = new ArrayList<>();

        data = storeDataInList();

        customAdapter = new CustomAdapter(Buy.this,data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Buy.this));
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
}
