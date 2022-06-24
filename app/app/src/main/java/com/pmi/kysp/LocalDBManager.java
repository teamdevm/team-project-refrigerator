package com.pmi.kysp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LocalDBManager {
    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    public LocalDBManager(Context context)
    {
        dbHelper = new DatabaseHelper(context);
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

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE, new String[]{});

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
}
