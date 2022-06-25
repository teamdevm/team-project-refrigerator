package com.pmi.kysp;

import static java.lang.Math.abs;

import android.util.Log;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Product {
    private String barcode, product_name, photo;
    private int category_id, energy, expiring_date;
    private double protein, fat, carbohydrate;

    public Product(String barcode, String product_name, String photo, int expiring_date, int category_id)
    {
        this.barcode = barcode;
        this.product_name = product_name;
        this.photo = photo;
        this.expiring_date = expiring_date;
        this.category_id = category_id;
    }

    public int getCategoryId()
    {
        return category_id;
    }

    public String getBarcode(){
        return barcode;
    }

    public String getProductName(){
        return product_name;
    }

    public String getImage(){
        return photo;
    }

    public String getDescription(){
        return String.format("Белки: %.2f\nЖиры: %.2f\nУглеводы: %.2f\nКкал: %d", protein, fat, carbohydrate, energy);
    }

    public void updateExpDate(LocalDate manufactureDate)
    {
        LocalDate expirationDate = manufactureDate.plusDays(expiring_date);

        expiring_date = (int)ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
    }

    public boolean isExpired()
    {
        return expiring_date < 0;
    }

    public String getExpDateString(){
        int expDays = abs(expiring_date);
        String expDate = Integer.toString(expDays) + " ";

        int rem = expDays % 10;

        if (expDays > 10 && expDays < 20)
            expDate += "дней";
        else if (rem % 10 == 1)
            expDate += "день";
        else if (rem == 0 || rem >= 5)
            expDate += "дней";
        else
            expDate += "дня";

        if (isExpired())
        {
            expDate = "просрочено на\n" + expDate;
        }

        return expDate;
    }
}
