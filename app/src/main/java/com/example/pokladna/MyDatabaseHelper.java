package com.example.pokladna;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "BeaversBar.db";
    private static int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "sklad";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "nazev";
    private static final String COLUMN_BUY = "kupni_cena";
    private static final String COLUMN_SELL = "prodejni_cena";
    private static final String COLUMN_AMMOUNT = "mnozstvi";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE "+ TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_BUY + " INTEGER, " +
                COLUMN_SELL + " INTEGER, " +
                COLUMN_AMMOUNT + " INTEGER);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    void addItem(Item item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME,item.getName());
        cv.put(COLUMN_BUY,item.getBuy());
        cv.put(COLUMN_SELL,item.getSell());
        cv.put(COLUMN_AMMOUNT,item.getAmmount());

        //TODO protect inserting of wrong data
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1)
        {
            Toast.makeText(context,context.getResources().getString(R.string.data_insert_0),Toast.LENGTH_SHORT).show();
            Log.e("db error","failed to insert " + result + " into database");
        }
        else
        {
            Toast.makeText(context,context.getResources().getString(R.string.data_insert_1),Toast.LENGTH_SHORT).show();
            //Toast.makeText(context,"succes",Toast.LENGTH_SHORT).show();
        }

    }
}
