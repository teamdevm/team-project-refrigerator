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

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Toast toast, toastError;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnScan = findViewById(R.id.footer__scan_barcode_button);
        ImageButton btnMain = findViewById(R.id.footer__main_button);
        ImageButton btnSettings = findViewById(R.id.footer__settings_button);

        btnMain.setActivated(true);

        CategoryAdapter categoryAdapter = new CategoryAdapter();
        RecyclerView categoryRecyclerView = findViewById(R.id.category_list);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerView.setAdapter(categoryAdapter);

        Category c1 = new Category(1,"Все", true);
        Category c2 = new Category(2, "Мясо", false);
        Category c3 = new Category(3, "Напитки", true);
        Category c4 = new Category(4, "Овощи", false);
        Category c5 = new Category(5, "Фрукты", false);
        Collection<Category> categories = Arrays.asList(c1, c2, c3, c4, c5);
        categoryAdapter.setCategories(categories);

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, Category category) {
                categoryAdapter.changeStateCategory(position);
            }
        });

        ProductAdapter productAdapter = new ProductAdapter();
        RecyclerView productRecyclerView = findViewById(R.id.product_list);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        productRecyclerView.addItemDecoration(new ProductItemDecoration((int)(0.04*width), (int)(0.1*height)));
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        productRecyclerView.setLayoutManager(layoutManager);
        productRecyclerView.setAdapter(productAdapter);
        String img = "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";
        Product p1 = new Product("123","Тест1",img,1, 1);
        Product p2 = new Product("123","Тест2","123",13, 1);
        Product p3 = new Product("123","Тест3","123",11, 1);
        Product p4 = new Product("123","Тест4",img,21, 1);
        Product p5 = new Product("123","Тест5,","123",1, 1);
        Product p6 = new Product("123","Тест5,","123",1, 1);
        Product p7 = new Product("123","Тест5,","123",1, 1);

        Collection<Product> products = Arrays.asList(p1, p2, p3, p4, p5, p6, p7);
        productAdapter.setProducts(products);

        productAdapter.setOnItemClickListener(new ProductAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, Product product) {

            }
        });

        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.create_db();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
            startActivity(newProductIntent);
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
