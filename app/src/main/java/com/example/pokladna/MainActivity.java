package com.example.pokladna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pokladna.BuySection.Buy;
import com.example.pokladna.DBStorage.MyDatabaseHelper;
import com.example.pokladna.Debts.Debt;
import com.example.pokladna.Debts.Debts;
import com.example.pokladna.EditSection.Storage;
import com.example.pokladna.SellSection.Sell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Boolean loaded = false;
    private Boolean sessionStarted = false;
    List<Item> items = new ArrayList<>();
    List<Debt> debts = new ArrayList<>();

    private final int REQUEST_PERMISSION_PHONE_STATE=1;

    Button buyButton, storageButton, sellButton, debtButton,sessionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        else Log.i("permission:", "permission is granted");

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 0);
        }
        else Log.i("permission:", "permission is granted");

        buyButton = findViewById(R.id.buyButton);
        storageButton = findViewById(R.id.storageButton);
        sellButton = findViewById(R.id.payButton);
        debtButton = findViewById(R.id.debtSectionButton);
        sessionButton = findViewById(R.id.sessionButton);

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

        sessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;
                SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
                if(!sharedPref.getBoolean("sessionStarted",false))
                {
                    Toast.makeText(context,context.getResources().getString(R.string.sessionStart),Toast.LENGTH_SHORT).show();
                    sessionButton.setText(context.getResources().getString(R.string.endSession));
                    //Start session
                    sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("sessionStarted", true);
                    editor.apply();

                    loadData();
                    writeData(context.getResources().getString(R.string.itemsStart),context.getResources().getString(R.string.debtsStart));


                }
                else
                {
                    //Stop session
                    Toast.makeText(context,context.getResources().getString(R.string.sessionEnd),Toast.LENGTH_SHORT).show();
                    sessionButton.setText(context.getResources().getString(R.string.startSession));
                    sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("sessionStarted", false);
                    editor.apply();

                    loadData();
                    writeData(context.getResources().getString(R.string.itemsEnd),context.getResources().getString(R.string.debtsEnd));
                    compareData();


                }
            }
        });


    }

    private void loadData()
    {
        MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
        Cursor cursor = myDB.readAllData();

        while(cursor.moveToNext())
        {
            Item item = new Item(Long.valueOf(cursor.getInt(0)),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),cursor.getString(5));
            items.add(item);
        }


        cursor = myDB.readAllDebts();
        while(cursor.moveToNext())
        {
            Debt debt = new Debt(Long.valueOf(cursor.getInt(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
            debts.add(debt);
        }
    }

    private void writeData(String filenameItem, String filenameDebts)
    {
        Context context = MainActivity.this;
        for (Item i: items)
        {
            writeToFile(i.getId().toString() + ",",context,filenameItem);
            writeToFile(i.getName() + ",",context,filenameItem);
            writeToFile(i.getBuy().toString() + ",",context,filenameItem);
            writeToFile(i.getSell().toString() + ",",context,filenameItem);
            writeToFile(i.getAmmount().toString() + ",",context,filenameItem);
            writeToFile(i.getProfile() + "\n",context,filenameItem);

        }

        for (Debt d: debts)
        {
            writeToFile(d.getId().toString() + ",",context,filenameDebts);
            writeToFile(d.getDebtor() + ",",context,filenameDebts);
            writeToFile(d.getDate() + ",",context,filenameDebts);
            writeToFile(d.getName() + ",",context,filenameDebts);
            writeToFile(d.getPrice().toString() + ",",context,filenameDebts);
            writeToFile(d.getAmmount().toString() + ",",context,filenameDebts);
            writeToFile(d.getProfile() + "\n",context,filenameDebts);

        }

    }

    private void compareData()
    {

    }

    private void writeToFile(String data,Context context,String filename) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + context.getResources().getString(R.string.cashRegister));
        if (!myDir.exists())
        {
            //TODO opravit tvorbu složky -> nevytváří se
                myDir.mkdirs();

        }
        if(!myDir.exists())Log.e("dir error","directory not created");
        File file = new File (myDir, filename);
        if (file.exists ()) file.delete ();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


}