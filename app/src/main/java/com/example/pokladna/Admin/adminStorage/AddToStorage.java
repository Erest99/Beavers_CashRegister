package com.example.pokladna.Admin.adminStorage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.DBStorage.MyDatabaseHelper;
import com.example.pokladna.InputHelper;
import com.example.pokladna.Item;
import com.example.pokladna.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class AddToStorage extends AppCompatActivity {

    Button addButton;
    EditText nameInput, buyInput, sellInput, amountInput, taxInput;
    Spinner spinner;
    ArrayList<String> profiles = new ArrayList<>();
    ArrayList<String> profilesS = new ArrayList<>();
    final String PROFILES = "profiles";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_admin);

        addButton = findViewById(R.id.updateButton);
        nameInput = findViewById(R.id.editTextNameUpdate);
        buyInput = findViewById(R.id.editTextBuyUpdate);
        sellInput = findViewById(R.id.editTextSellUpdate);
        amountInput = findViewById(R.id.editTextAmmountUpdate);
        taxInput = findViewById(R.id.taxInput);
        spinner = findViewById(R.id.spinner3);

        //load profiles from SP
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        profiles = new ArrayList<>(Arrays.asList(sharedPref.getString(PROFILES,"admin").split(",")));

        profilesS = new ArrayList<>(profiles);
        profilesS.remove(0);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddToStorage.this,android.R.layout.simple_spinner_item,profilesS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(AddToStorage.this);

                //Item item = new Item(nameInput.getText().toString().trim(),Integer.valueOf(buyInput.getText().toString().trim()),Integer.valueOf(sellInput.getText().toString().trim()),Integer.valueOf(amountInput.getText().toString().trim()));

                InputHelper helper = new InputHelper();

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

                    String profile = profilesS.get(spinner.getSelectedItemPosition());
                    Item item = new Item(helper.readText(nameInput).toLowerCase(Locale.ROOT),helper.readNumber(buyInput),helper.readNumber(sellInput),helper.readNumber(amountInput),helper.readNumber(taxInput),profile);

                    if(item.getAmmount()>0 && item.getAmmount()!=null) myDB.addItem(item,getApplicationContext());

                    Intent intent = new Intent(AddToStorage.this, Storage.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context,context.getResources().getString(R.string.wrongInput), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}