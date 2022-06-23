package com.pmi.kysp;

public class Product {
    private String barcode, product_name, photo;
    private int category_id, energy, expiring_date;
    private double protein, fat, carbohydrate;

    public Product(String barcode, String product_name, String photo, int expiring_date)
    {
        this.barcode = barcode;
        this.product_name = product_name;
        this.photo = photo;
        this.expiring_date = expiring_date;
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

    public String getExpDateString(){
        String expDate = Integer.toString(expiring_date) + " ";

        int rem = expiring_date % 10;

        if (expiring_date > 10 && expiring_date < 20)
            expDate += "дней";
        else if (rem % 10 == 1)
            expDate += "день";
        else if (rem == 0 || rem >= 5)
            expDate += "дней";
        else
            expDate += "дня";

        return expDate;
    }
}
