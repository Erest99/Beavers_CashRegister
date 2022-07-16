package com.example.pokladna.BuySection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.DBStorage.MyDatabaseHelper;
import com.example.pokladna.EditSection.Storage;
import com.example.pokladna.InputHelper;
import com.example.pokladna.Item;
import com.example.pokladna.MainActivity;
import com.example.pokladna.R;

import java.util.Locale;

public class BuyToStorage extends AppCompatActivity {

    Button addButton;
    EditText nameInput, buyInput, sellInput, amountInput, taxInput;

    int money;

    String[] profiles;
    String[] profilesMoney = {"cashAdmin","cash1","cash2","cash3","cash4","cash5","cash6","cash7","cash7","cash8","cash9"};
    final String PROFILES = "profiles";

    int activeProfile = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        addButton = findViewById(R.id.updateButton);
        nameInput = findViewById(R.id.editTextNameUpdate);
        buyInput = findViewById(R.id.editTextBuyUpdate);
        sellInput = findViewById(R.id.editTextSellUpdate);
        amountInput = findViewById(R.id.editTextAmmountUpdate);
        taxInput = findViewById(R.id.taxInput);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(BuyToStorage.this);
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
                    SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
                    String profile = sharedPref.getString("profile", "admin");
                    Item item = new Item(helper.readText(nameInput).toLowerCase(Locale.ROOT), helper.readNumber(buyInput),
                            helper.readNumber(sellInput), helper.readNumber(amountInput),helper.readNumber(taxInput),profile);


                    if(item.getAmmount()>0 && item.getAmmount()!=null)
                    {
                        myDB.addItem(item, context);
                        money -= item.getAmmount()* item.getBuy();
                    }

                    Intent intent = new Intent(BuyToStorage.this, Buy.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context,context.getResources().getString(R.string.wrongInput), Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        Intent intent = new Intent(BuyToStorage.this, Buy.class);
        startActivity(intent);
    }
}