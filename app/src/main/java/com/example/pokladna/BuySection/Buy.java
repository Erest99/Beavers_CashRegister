package com.example.pokladna.BuySection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.pokladna.DBStorage.MyDatabaseHelper;
import com.example.pokladna.EditSection.Storage;
import com.example.pokladna.Item;
import com.example.pokladna.MainActivity;
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
    TextView no_data,moneyTv;

    int money;

    String[] profiles;
    String[] profilesMoney = {"cashAdmin","cash1","cash2","cash3","cash4","cash5","cash6","cash7","cash7","cash8","cash9"};
    final String PROFILES = "profiles";

    int activeProfile = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_layout);

        recyclerView = findViewById(R.id.recyclerView);
        moneyTv = findViewById(R.id.moneyTextView6);
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
        moneyTv.setText(String.valueOf(money));

        customAdapter = new CustomAdapter(Buy.this,data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Buy.this));
    }

    List<Item> storeDataInList()
    {
        List<Item> items = new ArrayList<Item>();
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        String profile = sharedPref.getString("profile", "admin");
        Cursor cursor = myDB.readProfileData(profile);
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
                Item item = new Item(Long.valueOf(cursor.getInt(0)),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
                items.add(item);
            }
        }

        return items;
    }

    protected void onPause(){
        super.onPause();
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(profilesMoney[activeProfile], money);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(profilesMoney[activeProfile], money);
        editor.apply();

        Intent intent = new Intent(Buy.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        data = storeDataInList();

        customAdapter = new CustomAdapter(Buy.this,data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Buy.this));
    }


}
