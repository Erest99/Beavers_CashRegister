package com.example.pokladna.SellSection;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pokladna.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


public class QR extends AppCompatActivity {

    ImageView qrView;
    Button qrButton;

    String admin = "admin";
    String acko = "Atym";
    String bcko = "Btym";
    String[] profiles = {"penizeAdmin","penizeAtym","penizeB"};
    int activeProfile = 0;
    int amount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        qrView = findViewById(R.id.QR);
        qrButton = findViewById(R.id.qrButton2);
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set profile
                int money;
                SharedPreferences sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
                String profile = sharedPref.getString("profile", "admin");
                if(profile.equals(admin))activeProfile = 0;
                else if(profile.equals(acko))activeProfile = 1;
                else if(profile.equals(bcko))activeProfile = 2;

                sharedPref = getApplication().getSharedPreferences("BEAVERS",Context.MODE_PRIVATE);
                money = sharedPref.getInt(profiles[activeProfile], 0);
                money += amount;

                sharedPref = getApplication().getSharedPreferences("BEAVERS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(profiles[activeProfile], money);
                editor.apply();

            }
        });


        Intent intent = getIntent();
        amount = intent.getIntExtra("qr_cena",0);
        String barcode_content = "Účet test s částkou : " + String.valueOf(amount);

        Bitmap qrCode = createBitmap(barcode_content);
        qrView.setImageBitmap(qrCode);


    }

    private Bitmap createBitmap(String content)
    {
        BitMatrix result = null;
        try
        {
            result = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,400,400,null);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width*height];
        for(int x = 0; x<height;x++)
        {
            int offset = x*width;
            for(int k = 0; k< width;k++)
            {
                pixels[offset + k] = result.get(k,x) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels,0,width,0,0,width,height);
        return bitmap;
    }
}