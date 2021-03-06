package com.example.pokladna.Debts;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokladna.DBStorage.MyDatabaseHelper;
import com.example.pokladna.EditSection.Storage;
import com.example.pokladna.MainActivity;
import com.example.pokladna.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Debts extends AppCompatActivity {

    RecyclerView recyclerView;

    MyDatabaseHelper myDB;
    List<Debt> data;
    CustomAdapter customAdapter;

    ImageView empty_image;
    Button payButton, deleteButton;
    TextView no_data, moneyTv;
    static TextView payTv;
    List<Debt> debts;
    EditText filter;

    int money;

    String[] profiles;
    String[] profilesMoney = {"cashAdmin","cash1","cash2","cash3","cash4","cash5","cash6","cash7","cash7","cash8","cash9"};
    final String PROFILES = "profiles";

    int activeProfile = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debt_layout);

        recyclerView = findViewById(R.id.recyclerView);
        empty_image = findViewById(R.id.imageNoDataD);
        no_data = findViewById(R.id.textViewNoDataD);
        deleteButton = findViewById(R.id.dellButton);
        payButton = findViewById(R.id.payButton);
        filter = findViewById(R.id.debtET);
        payTv = findViewById(R.id.payTextView2);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                debts = customAdapter.getActive();
                for (Debt d: debts)
                {
                    if(d.getBeingPayed())
                    {
                        myDB.deleteOneDebt(d.getId().toString(),getApplicationContext());
                        data = storeDataInList();

                        customAdapter = new CustomAdapter(Debts.this, Debts.this,data);
                        recyclerView.setAdapter(customAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Debts.this));

                    }
                }
                payTv.setText("0");
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                debts = customAdapter.getActive();
                for (Debt d: debts)
                {
                    if(d.getBeingPayed())
                    {
                        myDB.deleteOneDebt(d.getId().toString(),getApplicationContext());
                        money+=d.getPrice();
                        moneyTv.setText(String.valueOf(money));
                        customAdapter.notifyDataSetChanged();
                        data = storeDataInList();

                        customAdapter = new CustomAdapter(Debts.this, Debts.this,data);
                        recyclerView.setAdapter(customAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Debts.this));
                    }
                }
                payTv.setText("0");
            }
        });

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Debt> search = new ArrayList<>();
                search=storeFilteredDataInList();
                if(filter.getText().toString().length()>0) {
                    customAdapter = new CustomAdapter(Debts.this, Debts.this, search);
                }else customAdapter = new CustomAdapter(Debts.this, Debts.this, data);
                    recyclerView.setAdapter(customAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Debts.this));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        myDB = new MyDatabaseHelper(Debts.this);
        data = new ArrayList<>();

        data = storeDataInList();

        customAdapter = new CustomAdapter(Debts.this, Debts.this,data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Debts.this));

        //get current money on register
        MainActivity mainActivity = new MainActivity();

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
        moneyTv = findViewById(R.id.moneyTextView);
        moneyTv.setText(String.valueOf(money));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            recreate();
        }

    }

    List<Debt> storeDataInList()
    {
        List<Debt> debts = new ArrayList<Debt>();
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        String profile = sharedPref.getString("profile", "admin");
        Cursor cursor = myDB.readProfileDebts(profile);

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
                Debt debt = new Debt(Long.valueOf(cursor.getInt(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
                debts.add(debt);

            }
        }

        return debts;
    }

    List<Debt> storeFilteredDataInList()
    {
        List<Debt> debts = new ArrayList<Debt>();
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        String profile = sharedPref.getString("profile", "admin");
        Cursor cursor = myDB.readProfileDebts(profile);
        String filerText = filter.getText().toString().toUpperCase(Locale.ROOT).trim();
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
                Debt debt = new Debt(Long.valueOf(cursor.getInt(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4),cursor.getInt(5),cursor.getString(6));
                if(debt.getDebtor().contains(filerText))debts.add(debt);
            }
        }

        return debts;
    }

    void confirmDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getApplicationContext().getResources().getString(R.string.deleteAll));
        builder.setMessage(getApplicationContext().getResources().getString(R.string.deleteAlllonger));
        builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(Debts.this);
                SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
                String profile = sharedPref.getString("profile", "admin");

                myDB.deleteAllProfileDebts(profile);
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

        Intent intent = new Intent(Debts.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        data = storeDataInList();

        customAdapter = new CustomAdapter(Debts.this, Debts.this,data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Debts.this));
    }

}
