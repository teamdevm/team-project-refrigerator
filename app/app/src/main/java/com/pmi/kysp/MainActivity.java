package com.pmi.kysp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int UPDATE_CONTAINERS = 1;
    Toast toast, toastError;
    ProductAdapter productAdapter;
    CategoryAdapter categoryAdapter;
    CategoryManager categoryManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnScan = findViewById(R.id.footer__scan_barcode_button);
        ImageButton btnMain = findViewById(R.id.footer__main_button);
        ImageButton btnSettings = findViewById(R.id.footer__settings_button);

        btnMain.setActivated(true);

        // Категории
        categoryAdapter = new CategoryAdapter();
        RecyclerView categoryRecyclerView = findViewById(R.id.category_list);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        // Продукты
        productAdapter = new ProductAdapter();
        RecyclerView productRecyclerView = findViewById(R.id.product_list);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        productRecyclerView.addItemDecoration(new ProductItemDecoration((int)(0.04*width), (int)(0.1*height)));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        productRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView.setAdapter(productAdapter);


        // Обработка нажатий
        loadData();

        productAdapter.setOnItemClickListener(new ProductAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, Product product) {
                Intent productIntent = new Intent(MainActivity.this, ProductActivity.class);
                productIntent.putExtra("barcode", product.getBarcode());
                startActivityForResult(productIntent, UPDATE_CONTAINERS);
            }
        });

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, Category category) {
                int categoryId = category.getCategoryId();

                categoryManager.changeStateOfCategory(categoryId);

                productAdapter.setProducts(categoryManager.getProducts());
                categoryAdapter.setCategories(categoryManager.getCategories());
            }
        });


        btnSettings.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toast != null) toast.cancel();
                if (toastError != null) toastError.cancel();
                BarcodeScanner.Scan(MainActivity.this);
            }
        });
    }

    private void loadData()
    {
        LocalDBManager localDBManager = new LocalDBManager(this);
        List<String> barcodes = localDBManager.getBarcodes();
        List<Product> products = ProductsApi.getProducts(barcodes);
        products.forEach(p -> {
            String barcode = p.getBarcode();
            LocalDate manufactureDate = null;
            try {
                manufactureDate = localDBManager.getManufactureDate(barcode);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            p.updateExpDate(manufactureDate);
        });
        List<Category> categories = CategoriesApi.getAllCategories();
        categoryManager = new CategoryManager(products, categories);

        categoryAdapter.setCategories(categoryManager.getCategories());
        productAdapter.setProducts(categoryManager.getProducts());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_CONTAINERS)
        {
            loadData();
            return;
        }

        String content = BarcodeScanner.Decode(requestCode, resultCode, data);

        if (content != null){
            int responseCode = ProductsApi.checkProduct(content);
            if (responseCode == -1){
                toastError = Toast.makeText(getApplicationContext(), "Не удалось считать штрих-код\nПроверьте подключение к интернету", Toast.LENGTH_LONG);
                toastError.show();
                return;
            }
            if (responseCode == 404){
                toastError = Toast.makeText(getApplicationContext(), "Данного продукта ещё нет в нашей базе", Toast.LENGTH_LONG);
                toastError.show();
                return;
            }
            Intent newProductIntent = new Intent(MainActivity.this, NewProductActivity.class);
            newProductIntent.putExtra("barcode", content);
            startActivityForResult(newProductIntent, UPDATE_CONTAINERS);

        }else{
            toast = Toast.makeText(getApplicationContext(), "Не удалось считать штрих-код", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
