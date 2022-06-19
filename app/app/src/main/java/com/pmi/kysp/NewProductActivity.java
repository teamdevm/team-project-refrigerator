package com.pmi.kysp;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewProductActivity extends AppCompatActivity {
    final Calendar productionDate = Calendar.getInstance();
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        initActivity();
    }

    protected void initActivity()
    {
        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.open();

        AppCompatButton addButton = (AppCompatButton)findViewById(R.id.new_product_activity__add_button);

        String barcode = getIntent().getExtras().getString("barcode");

        Product product = ProductsApi.getProduct(barcode);

        if (product == null){
            Toast.makeText(getApplicationContext(), "Не удалось считать штрих-код\nПроверьте подклчюение к интернету", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        String base64_ProductImage = product.getImage();
        setImage(base64_ProductImage);

        String productName = product.getProductName();
        setName(productName);

        String productDescription = product.getDescription();
        setDescription(productDescription);

        AppCompatButton productionDateButton = (AppCompatButton) findViewById(R.id.production_button);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                productionDate.set(Calendar.YEAR, year);
                productionDate.set(Calendar.MONTH, month);
                productionDate.set(Calendar.DAY_OF_MONTH, day);
                productionDateButton.setText((new SimpleDateFormat("dd.MM.yyyy")).format(productionDate.getTime()));
            }
        };
        productionDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(NewProductActivity.this, date, productionDate.get(Calendar.YEAR), productionDate.get(Calendar.MONTH), productionDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.COLUMN_BARCODE, product.getBarcode());
                NumericUpDownWidget numericUpDownWidget = (NumericUpDownWidget)findViewById(R.id.numeric);
                cv.put(DatabaseHelper.COLUMN_COUNT, numericUpDownWidget.getValue());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(productionDate.getTime());
                cv.put(DatabaseHelper.COLUMN_DATE_OF_MANUFACTURE, date);
                Cursor userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " + DatabaseHelper.COLUMN_BARCODE + "=?", new String[]{ product.getBarcode()});
                if (userCursor.getCount() == 0)
                    db.insert(DatabaseHelper.TABLE, null, cv);
                else
                    Toast.makeText(getApplicationContext(), "Данный продукт уже добавлен", Toast.LENGTH_LONG).show();
                db.close();
                userCursor.close();
                finish();
            }
        });
    }

    protected void setImage(String base64_image)
    {
        //if (base64_image.equals("")) return;
        ImageView productImageView = (ImageView) findViewById(R.id.product_image);
        byte[] decodedString = Base64.decode(base64_image, Base64.NO_WRAP);
        InputStream inputStream = new ByteArrayInputStream(decodedString);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        productImageView.setImageBitmap(bitmap);
    }

    protected void setName(String name)
    {
        TextView productName = (TextView) findViewById(R.id.product_name);
        productName.setText(name);
    }

    protected void setDescription(String description)
    {
        TextView productDescription = (TextView) findViewById(R.id.product_description);
        productDescription.setText(description);
    }
}