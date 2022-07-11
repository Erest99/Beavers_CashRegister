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
import com.example.pokladna.Item;
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
    List<Debt> debts;
    EditText filter;

    int money;

    String admin = "admin";
    String acko = "Atym";
    String bcko = "Btym";
    String[] profiles = {"penizeAdmin","penizeAtym","penizeB"};
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
        if(profile.equals(admin))activeProfile = 0;
        else if(profile.equals(acko))activeProfile = 1;
        else if(profile.equals(bcko))activeProfile = 2;

        sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        money = sharedPref.getInt(profiles[activeProfile], 0);
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
        editor.putInt(profiles[activeProfile], money);
        editor.apply();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(profiles[activeProfile], money);
//        editor.apply();
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(profiles[activeProfile], money);
//        editor.apply();
//    }
}
