package com.example.pokladna.EditSection;

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

import com.example.pokladna.Item;
import com.example.pokladna.MyDatabaseHelper;
import com.example.pokladna.R;

import java.util.ArrayList;
import java.util.List;

public class Storage extends AppCompatActivity {

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
        setContentView(R.layout.storage_layout);

        recyclerView = findViewById(R.id.recyclerView);
        empty_image = findViewById(R.id.imageNoDataS);
        no_data = findViewById(R.id.textViewNoDataS);
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

        customAdapter = new CustomAdapter(Storage.this, Storage.this,data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Storage.this));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.delete_all)
        {
            confirmDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all?");
        builder.setMessage("Are you sure you want to delete all data ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(Storage.this);
                myDB.deleteAllData();
                Toast.makeText(getApplicationContext(),"Deleting all data",Toast.LENGTH_SHORT).show();
                recreate();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}