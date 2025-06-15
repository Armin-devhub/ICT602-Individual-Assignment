package com.example.electricitybillestimator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Electricity.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "bills";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "month TEXT," +
                "year INTEGER," +
                "unit INTEGER," +
                "total REAL," +
                "rebate REAL," +
                "final REAL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertBill(Bill bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("month", bill.getMonth());
        values.put("year", bill.getYear());
        values.put("unit", bill.getUnitUsed());
        values.put("total", bill.getTotalCharges());
        values.put("rebate", bill.getRebate());
        values.put("final", bill.getFinalCost());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Bill> getAllBills() {
        ArrayList<Bill> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String month = cursor.getString(1);
                String year = cursor.getString(2);
                int unit = cursor.getInt(3);
                double total = cursor.getDouble(4);
                double rebate = cursor.getDouble(5);
                double finalCost = cursor.getDouble(6);
                list.add(new Bill(id, month, year, unit, total, rebate, finalCost));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean isMonthYearExist(String month, String year) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE month = ? AND year = ?",
                new String[]{month, String.valueOf(year)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // ✅ Delete a single bill by ID
    public void deleteBillById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ✅ Delete all bills from the table
    public void deleteAllBills() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
