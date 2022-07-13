package com.example.pokladna.Admin.adminStorage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokladna.DBStorage.MyDatabaseHelper;
import com.example.pokladna.Item;
import com.example.pokladna.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Storage extends AppCompatActivity {

    RecyclerView recyclerView;
    Button confirmButton;

    MyDatabaseHelper myDB;
    List<Item> data;
    CustomAdapter customAdapter;

    ImageView empty_image;
    TextView no_data;

    EditText filter;
    Spinner spinner;

    ArrayList<String> profiles = new ArrayList<>();

    ArrayList<String> profilesS = new ArrayList<>();
    final String PROFILES = "profiles";

    int activeProfile = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_storage_layout);

        spinner = findViewById(R.id.spinner4);
        recyclerView = findViewById(R.id.recyclerView);
        empty_image = findViewById(R.id.imageNoDataD);
        no_data = findViewById(R.id.textViewNoDataD);
        filter = findViewById(R.id.search2);
        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Storage.this, AddToStorage.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(Storage.this);
        data = new ArrayList<>();

        data = storeDataInList();



        customAdapter = new CustomAdapter(Storage.this, Storage.this, data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Storage.this));

        //load profiles from SP
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        profiles = new ArrayList<>(Arrays.asList(sharedPref.getString(PROFILES,"admin").split(",")));

        profilesS = new ArrayList<>(profiles);
        profilesS.remove(0);
        profilesS.add(0,getApplicationContext().getResources().getString(R.string.allData));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Storage.this,android.R.layout.simple_spinner_item,profilesS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Item> search = new ArrayList<>();
                search=storeFilteredDataInList();
                if(filter.getText().toString().length()>0) {
                    customAdapter = new CustomAdapter(Storage.this, Storage.this, search);
                }else customAdapter = new CustomAdapter(Storage.this, Storage.this, data);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Storage.this));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i == 0)
                {
                    data = new ArrayList<>();
                    data = storeDataInList();
                    customAdapter = new CustomAdapter(Storage.this, Storage.this, data);
                    recyclerView.setAdapter(customAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Storage.this));
                }
                else
                {
                    data = new ArrayList<>();
                    data = storeDataInList2(profiles.get(i));
                    customAdapter = new CustomAdapter(Storage.this, Storage.this, data);
                    recyclerView.setAdapter(customAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Storage.this));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                data = new ArrayList<>();
                data = storeDataInList();
                customAdapter = new CustomAdapter(Storage.this, Storage.this, data);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Storage.this));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }

    }

    List<Item> storeFilteredDataInList()
    {
        List<Item> items = new ArrayList<Item>();
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        Cursor cursor = myDB.readAllData();
        String filerText = filter.getText().toString().toLowerCase(Locale.ROOT).trim();
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
                if(item.getName().contains(filerText))items.add(item);
            }
        }

        return items;
    }

    List<Item> storeDataInList() {
        List<Item> items = new ArrayList<Item>();
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0) {
            Log.w("Data display", "no data to display");
            empty_image.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);

        } else {
            empty_image.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                Item item = new Item(Long.valueOf(cursor.getInt(0)), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4),cursor.getInt(5), cursor.getString(6));
                items.add(item);
            }
        }

        return items;
    }

    List<Item> storeDataInList2(String profile) {
        List<Item> items = new ArrayList<Item>();
        Cursor cursor = myDB.readProfileData(profile);
        if (cursor.getCount() == 0) {
            Log.w("Data display", "no data to display");
            empty_image.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);

        } else {
            empty_image.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                Item item = new Item(Long.valueOf(cursor.getInt(0)), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4),cursor.getInt(5), cursor.getString(6));
                items.add(item);
            }
        }

        return items;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all) {
            confirmDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getApplicationContext().getResources().getString(R.string.deleteAll));
        builder.setMessage(getApplicationContext().getResources().getString(R.string.deleteAlllonger));
        builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(Storage.this);
                SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
                String profile = sharedPref.getString("profile", "admin");
                myDB.deleteAllProfileData(profile);
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.deleting), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        myDB = new MyDatabaseHelper(Storage.this);
        data = new ArrayList<>();
        data = storeDataInList();

        customAdapter = new CustomAdapter(Storage.this, Storage.this, data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Storage.this));
    }
}
