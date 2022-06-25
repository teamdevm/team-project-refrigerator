package com.pmi.kysp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocalDBManager {
    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    public LocalDBManager(Context context)
    {
        dbHelper = new DatabaseHelper(context);
        dbHelper.create_db();
        db = dbHelper.open();
    }

    @Override
    public void finalize()
    {
        db.close();
        dbHelper.close();
    }

    public List<String> getBarcodes() {
        List<String> barcodes = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_BARCODE + " FROM " + DatabaseHelper.TABLE, new String[]{});

        if (cursor.getCount() == 0)
            return barcodes;

        cursor.moveToFirst();
        do {
            String barcode = cursor.getString(0);
            barcodes.add(barcode);
        } while (cursor.moveToNext());

        cursor.close();

        Log.d("barcodes", barcodes.toString());

        return barcodes;
    }

    public LocalDate getManufactureDate(String barcode) throws ParseException {
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_DATE_OF_MANUFACTURE + " FROM " + DatabaseHelper.TABLE + " WHERE " + DatabaseHelper.COLUMN_BARCODE + " = ?", new String[]{barcode});

        if (cursor.getCount() == 0)
            return LocalDate.now();

        cursor.moveToFirst();
        String dateManufactureString = cursor.getString(0);
        LocalDate manufactureDate = LocalDate.parse(dateManufactureString);

        cursor.close();

        return manufactureDate;
    }

    public void deleteProduct(String barcode)
    {
        db.delete(DatabaseHelper.TABLE, DatabaseHelper.COLUMN_BARCODE + "=?", new String[]{barcode});
    }

    public int getQuantity(String barcode)
    {
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_COUNT + " FROM " + DatabaseHelper.TABLE + " WHERE " + DatabaseHelper.COLUMN_BARCODE + " = ?", new String[]{barcode});

        if (cursor.getCount() == 0)
            return 0;

        cursor.moveToFirst();
        int quantity = cursor.getInt(0);
        cursor.close();

        return quantity;
    }
}
