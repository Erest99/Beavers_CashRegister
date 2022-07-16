package com.example.pokladna.Admin.adminStorage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.DBStorage.MyDatabaseHelper;
import com.example.pokladna.MainActivity;
import com.example.pokladna.R;

public class UpdateActivity extends AppCompatActivity {

    EditText nameInput, amountInput,buyInput,sellInput, taxInput, profileInput;
    Button confirmButton, deleteButton;

    String name,id, amount, buy, sell,tax,profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_update_storage);
        MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);


        nameInput = findViewById(R.id.editTextNameUpdate);
        amountInput = findViewById(R.id.editTextAmmountUpdate);
        buyInput = findViewById(R.id.editTextBuyUpdate);
        sellInput = findViewById(R.id.editTextSellUpdate);
        taxInput = findViewById(R.id.taxInput2);
        confirmButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        profileInput = findViewById(R.id.etProfile);

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
                tax = taxInput.getText().toString().trim();

                Context context = getApplicationContext();
                if( TextUtils.isDigitsOnly(buyInput.getText())
                        &&TextUtils.isDigitsOnly(sellInput.getText())
                        && TextUtils.isDigitsOnly(amountInput.getText())
                        &&nameInput.getText().toString().length()>0
                        &&buyInput.getText().toString().length()>0
                        &&sellInput.getText().toString().length()>0
                        &&amountInput.getText().toString().length()>0
                        &&taxInput.getText().toString().length()>0)
                {
                    SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
                    String profile = sharedPref.getString("profile", "admin");
                    myDB.updateData(id,name,amount,buy,sell,tax,profile,getApplicationContext());
                    Intent intent = new Intent(UpdateActivity.this, Storage.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context,context.getResources().getString(R.string.wrongInput), Toast.LENGTH_SHORT).show();
                }




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
            tax = getIntent().getStringExtra("tax");
            profile = getIntent().getStringExtra("profile");

            taxInput.setText(tax);
            nameInput.setText(name);
            amountInput.setText(amount);
            buyInput.setText(buy);
            sellInput.setText(sell);
            profileInput.setText(profile);
        }
        else
        {
            Log.e("getIntentData","Wrong data");
        }
    }

    void confirmDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getApplicationContext().getResources().getString(R.string.delete) +" "+ name + " ?");
        builder.setMessage(getApplicationContext().getResources().getString(R.string.confirmDelete)+" "+ name + " ?");
        builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id,getApplicationContext());
                Intent intent = new Intent(UpdateActivity.this, Storage.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UpdateActivity.this, Storage.class);
                startActivity(intent);

            }
        });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(UpdateActivity.this, Storage.class);
        startActivity(intent);

    }

}