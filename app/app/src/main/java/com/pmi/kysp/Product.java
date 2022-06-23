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
}
