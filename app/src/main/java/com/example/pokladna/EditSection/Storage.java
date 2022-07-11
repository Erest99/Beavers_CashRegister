package com.example.pokladna.EditSection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokladna.Item;
import com.example.pokladna.DBStorage.MyDatabaseHelper;
import com.example.pokladna.R;
import com.example.pokladna.SellSection.Sell;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Storage extends AppCompatActivity {

    RecyclerView recyclerView;
    Button confirmButton, balanceButton;

    MyDatabaseHelper myDB;
    List<Item> data;
    CustomAdapter customAdapter;

    ImageView empty_image;
    TextView no_data, moneyTv;

    int money;

    String admin = "admin";
    String acko = "Atym";
    String bcko = "Btym";
    String[] profiles = {"penizeAdmin", "penizeAtym", "penizeB"};
    int activeProfile = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_layout);

        recyclerView = findViewById(R.id.recyclerView);
        moneyTv = findViewById(R.id.moneyTextView4);
        empty_image = findViewById(R.id.imageNoDataD);
        no_data = findViewById(R.id.textViewNoDataD);
        balanceButton = findViewById(R.id.balanceButton);
        balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputDialog();
            }
        });
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

        //set profile
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        String profile = sharedPref.getString("profile", "admin");
        if (profile.equals(admin)) activeProfile = 0;
        else if (profile.equals(acko)) activeProfile = 1;
        else if (profile.equals(bcko)) activeProfile = 2;

        sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        money = sharedPref.getInt(profiles[activeProfile], 0);
        moneyTv.setText(String.valueOf(money));

        customAdapter = new CustomAdapter(Storage.this, Storage.this, data);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Storage.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }

    }

    List<Item> storeDataInList() {
        List<Item> items = new ArrayList<Item>();
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        String profile = sharedPref.getString("profile", "admin");
        Cursor cursor = myDB.readProfileData(profile);
        if (cursor.getCount() == 0) {
            Log.w("Data display", "no data to display");
            empty_image.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);

        } else {
            empty_image.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);

            while (cursor.moveToNext()) {
                Item item = new Item(Long.valueOf(cursor.getInt(0)), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5));
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

    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(profiles[activeProfile], money);
        editor.apply();
    }

    void inputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getApplicationContext().getResources().getString(R.string.balancedialogtitle));
        builder.setMessage(getApplicationContext().getResources().getString(R.string.balancedialog));
        final EditText balanceInput = new EditText(Storage.this);
        balanceInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(balanceInput);
        builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                money += Integer.valueOf(balanceInput.getText().toString().trim());

                Intent intent = new Intent(Storage.this, Storage.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

            }
        });
        builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.remove), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                money -= Integer.valueOf(balanceInput.getText().toString().trim());

                Intent intent = new Intent(Storage.this, Storage.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
        builder.create().show();
    }
}
