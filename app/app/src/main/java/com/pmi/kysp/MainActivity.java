package com.pmi.kysp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnScan = findViewById(R.id.footer__scan_barcode_button);
        ImageButton btnMain = findViewById(R.id.footer__main_button);
        btnMain.setActivated(true);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toast != null) toast.cancel();
                BarcodeScanner.Scan(MainActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String content = BarcodeScanner.Decode(requestCode, resultCode, data);

        if (content != null){
            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Результат");

            builder.setMessage(content);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
             */
            Intent newProductIntent = new Intent(MainActivity.this, NewProductActivity.class);
            newProductIntent.putExtra("barcode", content);
            startActivity(newProductIntent);
        }else{
            toast = Toast.makeText(getApplicationContext(), "Не удалось считать штрих-код", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
