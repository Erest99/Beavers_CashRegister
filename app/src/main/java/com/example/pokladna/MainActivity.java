package com.example.pokladna;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.pokladna.BuySection.Buy;
import com.example.pokladna.DBStorage.MyDatabaseHelper;
import com.example.pokladna.Debts.Debt;
import com.example.pokladna.Debts.Debts;
import com.example.pokladna.EditSection.Storage;
import com.example.pokladna.SellSection.Sell;
import com.example.pokladna.ui.Login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private Boolean loaded = false;
    private Boolean sessionStarted = false;
    List<Item> items = new ArrayList<>();
    List<Debt> debts = new ArrayList<>();
    int money;

    String[] profiles;
    String[] profilesMoney = {"cashAdmin","cash1","cash2","cash3","cash4","cash5","cash6","cash7","cash7","cash8","cash9"};
    String[] sessions = {"sessionAdmin","session1","session2","session3","session4","session5","session6","session7","session8","session9"};
    final String PROFILES = "profiles";

    int activeProfile = 0;

    private final int REQUEST_PERMISSION_PHONE_STATE=1;

    Button buyButton, storageButton, sellButton, debtButton,sessionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load profiles from SP
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        profiles = sharedPref.getString(PROFILES,"admin").split(",");



        //set profile

        String profile = sharedPref.getString("profile", "admin");
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

        //choose correct cash record
        money = sharedPref.getInt(profilesMoney[activeProfile], 0);



        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);





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
                if(!sharedPref.getBoolean(sessions[activeProfile],false))
                {
                    Toast.makeText(context,context.getResources().getString(R.string.sessionStart),Toast.LENGTH_SHORT).show();
                    sessionButton.setText(context.getResources().getString(R.string.endSession));
                    //Start session
                    sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(sessions[activeProfile], true);
                    editor.apply();

//                    loadData();
//                    writeData(context.getResources().getString(R.string.itemsStart),context.getResources().getString(R.string.debtsStart));

                    String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
                    //TODO prozkoumat - moc to nefuguje :D
                    Uri file = Uri.fromFile(new File(sdcard+"/Pokladna"));

                    loadData();
                    createFile(file, "test_start_" + Calendar.getInstance().getTime().toString() + ".txt" );

                   //openFile(file);
                    //openDirectory(dir);

                }
                else
                {
                    //Stop session
                    Toast.makeText(context,context.getResources().getString(R.string.sessionEnd),Toast.LENGTH_SHORT).show();
                    sessionButton.setText(context.getResources().getString(R.string.startSession));
                    sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(sessions[activeProfile], false);
                    editor.apply();

//                    loadData();
//                    writeData(context.getResources().getString(R.string.itemsEnd),context.getResources().getString(R.string.debtsEnd));
//                    compareData();

                    String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
                    Uri file = Uri.fromFile(new File(sdcard+"/Pokladna"));

                    loadData();
                    createFile(file,"test_konec_" + Calendar.getInstance().getTime().toString() + ".txt");
//                    openFile(file);
//                    openDirectory(dir);

                }
            }
        });

        if(!sharedPref.getBoolean(sessions[activeProfile],false))
        {
            sessionButton.setText(getApplicationContext().getResources().getString(R.string.startSession));

        }
        else
        {
            sessionButton.setText(getApplicationContext().getResources().getString(R.string.endSession));
        }


    }

    private void loadData()
    {
        MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
        Cursor cursor = myDB.readAllData();

        while(cursor.moveToNext())
        {
            Item item = new Item(Long.valueOf(cursor.getInt(0)),cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
            items.add(item);
        }


        cursor = myDB.readAllDebts();
        while(cursor.moveToNext())
        {
            Debt debt = new Debt(Long.valueOf(cursor.getInt(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
            debts.add(debt);
        }
    }

    // Request code for creating a PDF document.
    private static final int CREATE_FILE = 1;

    private void createFile(Uri pickerInitialUri, String filename) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_TITLE, filename);

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CREATE_FILE);
    }

    // Request code for selecting a PDF document.
    private static final int PICK_PDF_FILE = 2;

    private void openFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, PICK_PDF_FILE);
    }

    public void openDirectory(Uri uriToLoad) {
        // Choose a directory using the system's file picker.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);

        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 1


                && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                // Perform operations on the document using its URI.
                SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
                String title = "Záznam z akce:";
                if(!sharedPref.getBoolean(sessions[activeProfile],false))
                {
                    title= "Záznam ze začátku akce:";
                }
                else
                {
                    title= "Záznam ze konce akce:";
                }

                alterDocument(uri,title);
            }
        }
    }

    private void alterDocument(Uri uri,String title) {
        try {
            ParcelFileDescriptor pfd = MainActivity.this.getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());



            fileOutputStream.write((title+"\n").getBytes());
            fileOutputStream.write(("Overwritten at " + System.currentTimeMillis() +
                    "\n").getBytes());
            fileOutputStream.write(("Penize: ").getBytes());
            fileOutputStream.write((String.valueOf(money) + "\n").getBytes());

            fileOutputStream.write(("Odvod: ".getBytes()));
            MyDatabaseHelper myDB = new MyDatabaseHelper(getApplicationContext());
            //set profile
            SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
            String profile = sharedPref.getString("profile", "admin");
            fileOutputStream.write((String.valueOf(myDB.getProfileTax(profile)) + "\n").getBytes());

            fileOutputStream.write(("Zbozi:\n\n").getBytes());

            for (Item i: items)
        {
            fileOutputStream.write((i.getId().toString() + ",").getBytes());
            fileOutputStream.write((i.getName() + ",").getBytes());
            fileOutputStream.write((i.getBuy().toString() + ",").getBytes());
            fileOutputStream.write((i.getSell().toString() + ",").getBytes());
            fileOutputStream.write((i.getAmmount().toString() + ",").getBytes());
            fileOutputStream.write((i.getTax().toString() + ",").getBytes());
            fileOutputStream.write((i.getProfile() + "\n").getBytes());

        }

            fileOutputStream.write(("Dluhy:\n\n").getBytes());
        for (Debt d: debts)
        {
            fileOutputStream.write((d.getId().toString() + ",").getBytes());
            fileOutputStream.write((d.getDebtor() + ",").getBytes());
            fileOutputStream.write((d.getDate() + ",").getBytes());
            fileOutputStream.write((d.getName() + ",").getBytes());
            fileOutputStream.write((d.getPrice().toString() + ",").getBytes());
            fileOutputStream.write((d.getAmmount().toString() + ",").getBytes());
            fileOutputStream.write((d.getProfile() + "\n").getBytes());

        }

            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        items = new ArrayList<>();
        debts = new ArrayList<>();
    }

    private String readTextFromUri(Uri uri) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream inputStream =
                     getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }


    protected void onPause(){
        super.onPause();
        //save profile money
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
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
    }

}