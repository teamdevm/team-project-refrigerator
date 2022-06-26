package com.pmi.kysp;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

public class SettingsActivity extends Activity {
    private static final String[] DAYS = {"1 день", "2 дня", "3 дня", "4 дня", "5 дней", "6 дней", "7 дней", "8 дней", "9 дней", "10 дней"};
    int hour, minute;
    TextView timeText;
    SwitchMaterial switchMaterial;
    NumberPicker numberPicker;
    Toast toast, toastError;
    SharedPreferences sharedPreferences;
    final static String SWITCH_NOTIFICATIONS = "switch_notifications";
    final static String TIME_TEXT = "time_text";
    final static String NUMBER_PICKER_DAYS = "number_picker_days";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        numberPicker = findViewById(R.id.np);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setDisplayedValues(DAYS);

        switchMaterial = findViewById(R.id.settings__switch_notifications);


        timeText = findViewById(R.id.settings__time_text);
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePicker(v);
            }
        });
        loadPreferences();

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
    @Override
    protected void onStart(){
        super.onStart();
        loadPreferences();
    }

    @Override
    protected void onStop(){
        super.onStop();
        savePreferences();
        ProductsNotificationManager productsNotificationManager = new ProductsNotificationManager(getApplicationContext());
        productsNotificationManager.updateNotifications();
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        savePreferences();
        ProductsNotificationManager productsNotificationManager = new ProductsNotificationManager(getApplicationContext());
        productsNotificationManager.updateNotifications();
    }


    private void savePreferences(){
        sharedPreferences = getSharedPreferences("SettingsNotification", MODE_PRIVATE);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putBoolean(SWITCH_NOTIFICATIONS, switchMaterial.isChecked());
        ed.putString(TIME_TEXT, timeText.getText().toString());
        ed.putInt(NUMBER_PICKER_DAYS, numberPicker.getValue());
        ed.commit();
    }

    private void loadPreferences(){
        sharedPreferences = getSharedPreferences("SettingsNotification", MODE_PRIVATE);
        boolean switchNotificationsValue = sharedPreferences.getBoolean(SWITCH_NOTIFICATIONS, true);
        String timeTextValue = sharedPreferences.getString(TIME_TEXT, "17:00");
        int npDaysValue = sharedPreferences.getInt(NUMBER_PICKER_DAYS, 1);
        setPreferences(switchNotificationsValue, timeTextValue, npDaysValue);
    }

    private void setPreferences(boolean switchNotifications, String timeTextValue, int npDaysValue){
        switchMaterial.setChecked(switchNotifications);
        timeText.setText(timeTextValue);
        numberPicker.setValue(npDaysValue);
    }
}
