package com.example.pokladna.SellSection;

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

public class Sell extends AppCompatActivity {

    RecyclerView recyclerView;
    int money;

    Button sellButton, debtButton, qrButton;
    MyDatabaseHelper myDB;
    List<Item> data;
    CustomAdapter customAdapter;
    List<Item> cart;

    ImageView empty_image;
    TextView no_data;
    TextView moneyTv;
    static TextView priceTv;
    EditText filter;

    String[] profiles;
    String[] profilesMoney = {"cashAdmin","cash1","cash2","cash3","cash4","cash5","cash6","cash7","cash7","cash8","cash9"};
    final String PROFILES = "profiles";

    int activeProfile = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_layout);

        recyclerView = findViewById(R.id.recyclerView);
        empty_image = findViewById(R.id.imageViewNoDataSS);
        no_data = findViewById(R.id.textViewNoDataSS);
        qrButton = findViewById(R.id.qrButton);
        filter = findViewById(R.id.search);
        priceTv = findViewById(R.id.payTextView);
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cart = customAdapter.getCart();


                int sum = 0;
                for (Item i:cart)
                {
                    sum += i.getSell()*i.getAmmount();
                }
                Intent intent = new Intent(Sell.this, QR.class);
                intent.putExtra("qr_cena",sum);
                startActivity(intent);

                priceTv.setText("0");
            }
        });
        sellButton = findViewById(R.id.payButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cart contains selected items
                cart = customAdapter.getCart();
                sellCart(cart);
                int sum = 0;
                for (Item i:cart)
                {
                    sum += i.getSell()*i.getAmmount();
                }
                money+=sum;
                moneyTv.setText(String.valueOf(money));
                customAdapter.notifyDataSetChanged();

                data = storeDataInList();

                customAdapter = new CustomAdapter(Sell.this, Sell.this,data);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Sell.this));

                priceTv.setText("0");
            }
        });
        debtButton = findViewById(R.id.dellButton);
        debtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creates debt record
                cart = customAdapter.getCart();
                for (Item i:cart)
                {
                    i.setSell(i.getSell()*i.getAmmount());
                    i.setBuy(i.getBuy()*i.getAmmount());
                }
                inputDialog(cart);

                priceTv.setText("0");
            }
        });

        myDB = new MyDatabaseHelper(Sell.this);
        data = new ArrayList<>();

        data = storeDataInList();

        customAdapter = new CustomAdapter(Sell.this, Sell.this,data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Sell.this));

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

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Item> search = new ArrayList<>();
                search=storeFilteredDataInList();
                if(filter.getText().toString().length()>0) {
                    customAdapter = new CustomAdapter(Sell.this, Sell.this, search);
                }else customAdapter = new CustomAdapter(Sell.this, Sell.this, data);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(Sell.this));


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void updatePayAmount()
    {
        int price = 0;
        cart = customAdapter.getCart();
        for (Item i:cart) {

            price += i.getSell()*i.getAmmount();
            priceTv.setText(String.valueOf(price));
        }
    }

    List<Item> storeFilteredDataInList()
    {
        List<Item> items = new ArrayList<Item>();
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        String profile = sharedPref.getString("profile", "admin");
        Cursor cursor = myDB.readProfileData(profile);
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


    void inputDialog(List<Item> cart)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getApplicationContext().getResources().getString(R.string.inputDialog1));
        builder.setMessage(getApplicationContext().getResources().getString(R.string.inputDialog2));
        final EditText debtInput = new EditText(Sell.this);
        builder.setView(debtInput);
        builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(Sell.this);
                List<Item> helper = new ArrayList<>();
                for (Item i :cart)
                {
                    if(i.getAmmount()>0)   helper.add(i);
                }
                if(debtInput.getText().toString().trim().length()>0) {
                    myDB.addDebt(helper, debtInput.getText().toString().trim().toUpperCase(Locale.ROOT), getApplicationContext());
                    sellCart(helper);
                    //recreate();
                    Intent intent = new Intent(Sell.this, Sell.class);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                else
                {
                    Toast.makeText(Sell.this, "Nelze vytvořit nepodepsaný dluh, neuloženo!", Toast.LENGTH_SHORT).show();
                    inputDialog(cart);
                }
            }
        });
        builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    void sellCart(List<Item> cart)
    {
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        String profile = sharedPref.getString("profile", "admin");
        for (Item i: cart)
        {
            Cursor cursor = myDB.findById(i.getId().intValue());
            while(cursor.moveToNext())
            {
                if(cursor.getInt(4)-i.getAmmount()<1)myDB.deleteOneRow(String.valueOf(cursor.getInt(0)),Sell.this);
                else
                {
                    myDB.updateData(String.valueOf(cursor.getInt(0)),
                        cursor.getString(1),
                        String.valueOf(cursor.getInt(4)-i.getAmmount()),
                        String.valueOf(cursor.getInt(2)),
                        String.valueOf(cursor.getInt(3)),
                        String.valueOf(cursor.getInt(5)),
                        profile,
                        getApplicationContext());
                }

            }

        }
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
        Intent intent = new Intent(Sell.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        data = storeDataInList();

        customAdapter = new CustomAdapter(Sell.this, Sell.this,data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Sell.this));

    }
}
