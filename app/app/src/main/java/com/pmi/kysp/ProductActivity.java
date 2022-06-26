package com.pmi.kysp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;

public class ProductActivity extends AppCompatActivity {
    Product product;
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
        product = null;
        LocalDBManager localDBManager = new LocalDBManager(this);

        AppCompatButton deleteButton = (AppCompatButton) findViewById(R.id.product_activity__del_button);

        String barcode = getIntent().getExtras().getString("barcode");
        Log.d("barcode", barcode);
        product = ProductsApi.getProduct(barcode);
        if (product == null){
            Toast.makeText(getApplicationContext(), "Не удалось загрузить информацию о продукте.\nПроверьте подклчюение к интернету", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        product.updateExpDate(localDBManager.getManufactureDate(barcode));

        int quantity = localDBManager.getQuantity(barcode);
        NumericUpDownWidget numericUpDownWidget = (NumericUpDownWidget) findViewById(R.id.numeric);
        numericUpDownWidget.setValue(quantity);
        numericUpDownWidget.setOnValueChangeListener(new NumericUpDownWidget.ValueChangeListener() {
            @Override
            public void onValueChange(int value) {
                localDBManager.updateQuantity(barcode, value);
            }
        });

        setProductInfo(product);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductsNotificationManager productsNotificationManager = new ProductsNotificationManager(getApplicationContext());
                productsNotificationManager.disableNotification(product);
                localDBManager.deleteProduct(barcode);
                finish();
            }
        });
    }

    protected void setProductInfo(Product product)
    {
        AppCompatButton expDateBtn = (AppCompatButton) findViewById(R.id.production_button);
        ImageView productImageView = (ImageView) findViewById(R.id.product_image);
        TextView productName = (TextView) findViewById(R.id.product_name);
        TextView productDescription = (TextView) findViewById(R.id.product_description);


        expDateBtn.setText(product.getExpDateString());
        if (product.isExpired())
        {
            expDateBtn.setActivated(true);
        }

        byte[] decodedString = Base64.decode(product.getImage(), Base64.NO_WRAP);
        InputStream inputStream = new ByteArrayInputStream(decodedString);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        productImageView.setImageBitmap(bitmap);

        productName.setText(product.getProductName());

        productDescription.setText(product.getDescription());
    }
}
