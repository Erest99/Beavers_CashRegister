package com.example.pokladna.DBStorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pokladna.Item;
import com.example.pokladna.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "BeaversBar.db";
    private static int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "sklad";
    private static final String TABLE_NAME2 = "dluhy";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "nazev";
    private static final String COLUMN_BUY = "kupni_cena";
    private static final String COLUMN_SELL = "prodejni_cena";
    private static final String COLUMN_AMMOUNT = "mnozstvi";
    private static final String COLUMN_PROFILE = "profil";
    private static final String COLUMN_DEBTOR = "dluznik";
    private static final String COLUMN_DATE = "datum";
    private static final String COLUMN_PRICE = "cena";
    private static final String COLUMN_TAX = "odvod";


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
                COLUMN_TAX + " INTEGER, " +
                COLUMN_PROFILE + " TEXT);";

        db.execSQL(query);

        query =
                "CREATE TABLE "+ TABLE_NAME2 + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_DEBTOR + " TEXT, " +
                        COLUMN_DATE + " TEXT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_PRICE + " INTEGER, " +
                        COLUMN_AMMOUNT + " INTEGER, " +
                        COLUMN_PROFILE + " TEXT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);

    }

    public void addItem(Item item, Context context)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME,item.getName());
        cv.put(COLUMN_BUY,item.getBuy());
        cv.put(COLUMN_SELL,item.getSell());
        cv.put(COLUMN_AMMOUNT,item.getAmmount());
        cv.put(COLUMN_TAX,item.getTax());
        cv.put(COLUMN_PROFILE,item.getProfile());

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

    public void addDebt(List<Item> items,String debtor, Context context)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        for (Item item: items)
        {
            cv.put(COLUMN_NAME,item.getName());
            cv.put(COLUMN_PRICE,item.getSell());
            cv.put(COLUMN_AMMOUNT,item.getAmmount());
            cv.put(COLUMN_PROFILE,item.getProfile());
            cv.put(COLUMN_DEBTOR, debtor);
            Date c = Calendar.getInstance().getTime();
            cv.put(COLUMN_DATE,c.toString());

            long result = db.insert(TABLE_NAME2, null, cv);
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

    public Cursor readAllDebts()
    {
        String query = "SELECT * FROM " + TABLE_NAME2;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
        {
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor readProfileData(String profile)
    {

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_PROFILE + " = "+ "\""+profile+"\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
        {
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Integer getProfileTax(String profile)
    {

        int tax = 0;
        String query = "SELECT "+COLUMN_TAX+" FROM " + TABLE_NAME + " WHERE " + COLUMN_PROFILE + " = "+ "\""+profile+"\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
        {
            cursor = db.rawQuery(query,null);
        }

        while (cursor.moveToNext()) {
            tax+=cursor.getInt(0);
        }
        return tax;
    }

    public Cursor readProfileDebts(String profile)
    {

        String query = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + COLUMN_PROFILE + " = "+ "\""+profile+"\"";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
        {
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }




    public void updateData(String row_id, String name, String amount, String buy, String sell, String tax, String profile, Context context )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_AMMOUNT, amount);
        cv.put(COLUMN_BUY, buy);
        cv.put(COLUMN_SELL, sell);
        cv.put(COLUMN_TAX,tax);
        cv.put(COLUMN_PROFILE,profile);

        long result = db.update(TABLE_NAME,cv,"_id=?",new String[]{row_id});
        if (result ==-1)
        {
            //Toast.makeText(context,context.getResources().getString(R.string.data_update_0), Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Toast.makeText(context,context.getResources().getString(R.string.data_update_1), Toast.LENGTH_SHORT).show();
        }

    }

    public void updateDebts(String row_id,String debtor, String date, String name, String amount, String price, String profile, Context context )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DEBTOR,debtor);
        cv.put(COLUMN_DATE,date);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_AMMOUNT, amount);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_PROFILE,profile);

        long result = db.update(TABLE_NAME2,cv,"_id=?",new String[]{row_id});
        if (result ==-1)
        {
            //Toast.makeText(context,context.getResources().getString(R.string.data_update_0), Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Toast.makeText(context,context.getResources().getString(R.string.data_update_1), Toast.LENGTH_SHORT).show();
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

    public void deleteOneDebt(String row_id,Context context)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME2,"_id=?",new String[]{row_id});
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

    public void deleteAllProfileData(String profile)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_PROFILE + " = "+ "\""+profile+"\"" );
    }

    public void deleteAllProfileDebts(String profile)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME2 + " WHERE " + COLUMN_PROFILE + " = "+ "\""+profile+"\"" );
    }


    public Cursor findById(Integer id)
    {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE _id = " + String.valueOf(id);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null)
        {
            cursor = db.rawQuery(query,null);
        }
        return cursor;

    }

}
