package com.pmi.kysp;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.Locale;

public class SettingsActivity extends Activity {
    private static final String[] DAYS = {"1 день", "2 дня", "3 дня", "4 дня", "5 дней", "6 дней", "7 дней", "8 дней", "9 дней", "10 дней"};
    int hour, minute;
    TextView timeText;
    Toast toast, toastError;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        NumberPicker numberPicker = findViewById(R.id.np);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setDisplayedValues(DAYS);

        // TODO: Запоминание времени + подгрузка при открытии настроек
        timeText = findViewById(R.id.settings__time_text);
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePicker(v);
            }
        });

        // TODO: Более красивое решение нужно
        ImageButton btnScan = findViewById(R.id.footer__scan_barcode_button);
        ImageButton btnMain = findViewById(R.id.footer__main_button);
        ImageButton btnSettings = findViewById(R.id.footer__settings_button);

        btnSettings.setActivated(true);
        btnMain.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toast != null) toast.cancel();
                if (toastError != null) toastError.cancel();
                BarcodeScanner.Scan(SettingsActivity.this);
            }
        });
    }
    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeText.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Время уведомления");
        timePickerDialog.show();
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
            Intent newProductIntent = new Intent(SettingsActivity.this, NewProductActivity.class);
            newProductIntent.putExtra("barcode", content);
            startActivity(newProductIntent);
        }else{
            toast = Toast.makeText(getApplicationContext(), "Не удалось считать штрих-код", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
