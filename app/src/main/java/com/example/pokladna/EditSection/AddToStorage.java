package com.example.pokladna.EditSection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.InputHelper;
import com.example.pokladna.Item;
import com.example.pokladna.MyDatabaseHelper;
import com.example.pokladna.R;

public class AddToStorage extends AppCompatActivity {

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

                MyDatabaseHelper myDB = new MyDatabaseHelper(AddToStorage.this);

                //Item item = new Item(nameInput.getText().toString().trim(),Integer.valueOf(buyInput.getText().toString().trim()),Integer.valueOf(sellInput.getText().toString().trim()),Integer.valueOf(amountInput.getText().toString().trim()));

                InputHelper helper = new InputHelper();
                Item item = new Item(helper.readText(nameInput),helper.readNumber(buyInput),helper.readNumber(sellInput),helper.readNumber(amountInput));

                myDB.addItem(item,getApplicationContext());

                Intent intent = new Intent(AddToStorage.this, Storage.class);
                startActivity(intent);
            }
        });
    }
}