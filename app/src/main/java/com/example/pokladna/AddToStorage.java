package com.example.pokladna;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddToStorage extends AppCompatActivity {

    Button addButton;
    EditText nameInput, buyInput, sellInput, amountInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buy);

        addButton = findViewById(R.id.confirmButton2);
        nameInput = findViewById(R.id.editTextName);
        buyInput = findViewById(R.id.editTextBuy);
        sellInput = findViewById(R.id.editTextSell);
        amountInput = findViewById(R.id.editTextAmmount);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(AddToStorage.this);

                //Item item = new Item(nameInput.getText().toString().trim(),Integer.valueOf(buyInput.getText().toString().trim()),Integer.valueOf(sellInput.getText().toString().trim()),Integer.valueOf(amountInput.getText().toString().trim()));

                InputHelper helper = new InputHelper();
                Item item = new Item(helper.readText(nameInput),helper.readNumber(buyInput),helper.readNumber(sellInput),helper.readNumber(amountInput));

                myDB.addItem(item);
            }
        });
    }
}