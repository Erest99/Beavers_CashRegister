package com.example.pokladna;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    private static final String COLUMN_PROFILE = "profil";


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
                COLUMN_AMMOUNT + " INTEGER, " +
                COLUMN_PROFILE + " TEXT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void addItem(Item item,Context context)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME,item.getName());
        cv.put(COLUMN_BUY,item.getBuy());
        cv.put(COLUMN_SELL,item.getSell());
        cv.put(COLUMN_AMMOUNT,item.getAmmount());
        cv.put(COLUMN_PROFILE,"BEAVERS_TEST");

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

    public Cursor readAllData()
    {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
        {
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public void updateData(String row_id, String name, String amount, String buy, String sell, Context context )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_AMMOUNT, amount);
        cv.put(COLUMN_BUY, buy);
        cv.put(COLUMN_SELL, sell);
        cv.put(COLUMN_PROFILE,"BEAVERS_TEST");

        long result = db.update(TABLE_NAME,cv,"_id=?",new String[]{row_id});
        if (result ==-1)
        {
            Toast.makeText(context,context.getResources().getString(R.string.data_update_0), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context,context.getResources().getString(R.string.data_update_1), Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteOneRow(String row_id,Context context)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME,"_id=?",new String[]{row_id});
        if (result == -1)
        {
            Toast.makeText(context,context.getResources().getString(R.string.data_delete_0), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context,context.getResources().getString(R.string.data_delete_1), Toast.LENGTH_SHORT).show();
        }
    }

    public void dropTable()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query =
                "DROP TABLE "+TABLE_NAME;

        db.execSQL(query);
        onCreate(db);
    }

    public void createTable()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query =
                "CREATE TABLE "+ TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_BUY + " INTEGER, " +
                        COLUMN_SELL + " INTEGER, " +
                        COLUMN_AMMOUNT + " INTEGER, " +
                        COLUMN_PROFILE + " TEXT);";

        db.execSQL(query);
    }

    public void deleteAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

}
