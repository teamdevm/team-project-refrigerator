package com.pmi.kysp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;

public class ProductActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        try {
            initActivity();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    protected void initActivity() throws ParseException {
        LocalDBManager localDBManager = new LocalDBManager(this);

        AppCompatButton deleteButton = (AppCompatButton) findViewById(R.id.product_activity__del_button);

        String barcode = getIntent().getExtras().getString("barcode");
        Log.d("barcode", barcode);
        Product product = ProductsApi.getProduct(barcode);
        product.updateExpDate(localDBManager.getManufactureDate(barcode));

        setProductInfo(product);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localDBManager.deleteProduct(barcode);
                finish();
            }
        });

        // TODO: количество?
    }

    protected void setProductInfo(Product product)
    {
        AppCompatButton expDateBtn = (AppCompatButton) findViewById(R.id.production_button);
        ImageView productImageView = (ImageView) findViewById(R.id.product_image);
        TextView productName = (TextView) findViewById(R.id.product_name);
        TextView productDescription = (TextView) findViewById(R.id.product_description);


        expDateBtn.setText(product.getExpDateString());

        byte[] decodedString = Base64.decode(product.getImage(), Base64.NO_WRAP);
        InputStream inputStream = new ByteArrayInputStream(decodedString);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        productImageView.setImageBitmap(bitmap);

        productName.setText(product.getProductName());

        productDescription.setText(product.getDescription());
    }
}
