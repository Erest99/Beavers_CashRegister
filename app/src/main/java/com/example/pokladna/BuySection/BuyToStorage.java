package com.example.pokladna.BuySection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pokladna.InputHelper;
import com.example.pokladna.Item;
import com.example.pokladna.MyDatabaseHelper;
import com.example.pokladna.R;

public class BuyToStorage extends AppCompatActivity {

    Button addButton;
    EditText nameInput, buyInput, sellInput, amountInput;

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
                    Item item = new Item(helper.readText(nameInput), helper.readNumber(buyInput), helper.readNumber(sellInput), helper.readNumber(amountInput));

                    myDB.addItem(item, context);

                    Intent intent = new Intent(BuyToStorage.this, Buy.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(context,context.getResources().getString(R.string.wrongInput), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}