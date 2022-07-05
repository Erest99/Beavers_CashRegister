package com.example.pokladna;

import android.widget.EditText;

import java.util.Locale;

public class InputHelper {
    public String readText(EditText editText)
    {
        return (editText.getText().toString().trim());
    }

    public Integer readNumber(EditText editText)
    {
        return (Integer.valueOf(editText.getText().toString().trim()));
    }
}
