package com.example.pokladna.EditSection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.MyDatabaseHelper;
import com.example.pokladna.R;

public class UpdateActivity extends AppCompatActivity {

    EditText nameInput, amountInput,buyInput,sellInput;
    Button confirmButton, deleteButton;

    String name,id, amount, buy, sell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_layout);
        MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);


        nameInput = findViewById(R.id.editTextNameUpdate);
        amountInput = findViewById(R.id.editTextAmmountUpdate);
        buyInput = findViewById(R.id.editTextBuyUpdate);
        sellInput = findViewById(R.id.editTextSellUpdate);
        confirmButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

        //prepare data
        getAndSetIntentData();

        //set action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(name);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString().trim();
                amount = amountInput.getText().toString().trim();
                buy = buyInput.getText().toString().trim();
                sell = sellInput.getText().toString().trim();


                myDB.updateData(id,name,amount,buy,sell,getApplicationContext());
                Intent intent = new Intent(UpdateActivity.this, Storage.class);
                startActivity(intent);


            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmDialog();

            }
        });


    }

    void getAndSetIntentData()
    {
        if(getIntent().hasExtra("id")&&getIntent().hasExtra("name")&&getIntent().hasExtra("amount")&&getIntent().hasExtra("buy")&&
                getIntent().hasExtra("sell"))
        {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            amount = getIntent().getStringExtra("amount");
            buy = getIntent().getStringExtra("buy");
            sell = getIntent().getStringExtra("sell");

            nameInput.setText(name);
            amountInput.setText(amount);
            buyInput.setText(buy);
            sellInput.setText(sell);
        }
        else
        {
            Log.e("getIntentData","Wrong data");
        }
    }

    void confirmDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + "?");
        builder.setMessage("Are you sure you want to delete "+ name + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id,getApplicationContext());
                Intent intent = new Intent(UpdateActivity.this, Storage.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UpdateActivity.this, Storage.class);
                startActivity(intent);

            }
        });
        builder.create().show();
    }

}