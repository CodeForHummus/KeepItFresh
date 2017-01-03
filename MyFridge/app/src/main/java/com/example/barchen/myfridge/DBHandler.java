package com.example.barchen.myfridge;

/**
 * Created by pagi on 1/2/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "merchandiseInfo";
    // Contacts table name
    private static final String TABLE_MERCHANDISE = "merchandise";
    // Merchandise Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_DAYS = "days";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MERCHANDISE_TABLE = "CREATE TABLE " + TABLE_MERCHANDISE + "("
                + KEY_ID    + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME  + "TEXT,"
                + KEY_DATE  + "INTEGER"
                + KEY_DAYS  + "INTEGER"                                       + ")";
        db.execSQL(CREATE_MERCHANDISE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_MERCHANDISE);
// Creating tables again
        onCreate(db);
    }

    // Adding new merchandise
    public void addMerchandise(MyFridgeDatabase merchandise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, merchandise.getName()); // Merchandise Name
        values.put(KEY_DATE, merchandise.getDate()); // Merchandise date
        values.put(KEY_DAYS, merchandise.getDays()); // Merchandise days left
// Inserting Row
        db.insert(TABLE_MERCHANDISE, null, values);
        db.close(); // Closing database connection
    }

    // Getting one merchandise
    public MyFridgeDatabase getMerchandise(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MERCHANDISE, new String[] { KEY_ID,
                        KEY_NAME, KEY_DATE, KEY_DAYS }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        MyFridgeDatabase merch = new MyFridgeDatabase(
                cursor.getString(1), Integer.parseInt(cursor.getString(2)), Integer.parseInt(cursor.getString(3))
        );
// return merchandise
        return merch;
    }

    // Getting All merchandise
    public List<MyFridgeDatabase> getAllMerchandise() {
        List<MyFridgeDatabase> merchList = new ArrayList<MyFridgeDatabase>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MERCHANDISE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MyFridgeDatabase merch = new MyFridgeDatabase();
                merch.setName(cursor.getString(1));
                merch.setDate(Integer.parseInt(cursor.getString(2)));
                merch.setDays(Integer.parseInt(cursor.getString(3)));
// Adding contact to list
                merchList.add(merch);
            } while (cursor.moveToNext());
        }
// return contact list
        return merchList;
    }

    // Getting merchandise Count
    public int getShopsCount() {
        String countQuery = "SELECT * FROM " + TABLE_MERCHANDISE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
// return count
        return cursor.getCount();
    }

    // Updating merchandise
    public int updateShop(MyFridgeDatabase merch) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, merch.getName());
        values.put(KEY_DATE, merch.getDate());
        values.put(KEY_DAYS, merch.getDays());
// updating row
        return db.update(TABLE_MERCHANDISE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(merch.getId())});
    }


    // Deleting a merchandise
    public void deleteShop(MyFridgeDatabase shop) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MERCHANDISE, KEY_ID + " = ?",
                new String[] { String.valueOf(shop.getId()) });
        db.close();
    }
}
