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
import com.example.pokladna.InputHelper;
import com.example.pokladna.Item;
import com.example.pokladna.R;

import java.util.Locale;

public class BuyToStorage extends AppCompatActivity {

    Button addButton;
    EditText nameInput, buyInput, sellInput, amountInput;

    int money;

    String admin = "admin";
    String acko = "Atym";
    String bcko = "Btym";
    String[] profiles = {"penizeAdmin","penizeAtym","penizeB"};
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
                        &&amountInput.getText().toString().length()>0)
                {
                    SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
                    String profile = sharedPref.getString("profile", "admin");
                    Item item = new Item(helper.readText(nameInput).toLowerCase(Locale.ROOT), helper.readNumber(buyInput), helper.readNumber(sellInput), helper.readNumber(amountInput),profile);


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
        if(profile.equals(admin))activeProfile = 0;
        else if(profile.equals(acko))activeProfile = 1;
        else if(profile.equals(bcko))activeProfile = 2;

        sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
        money = sharedPref.getInt(profiles[activeProfile], 0);

    }
    protected void onPause(){
        super.onPause();
        SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(profiles[activeProfile], money);
        editor.apply();
    }
}