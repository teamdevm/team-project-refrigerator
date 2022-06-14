package com.pmi.kysp;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import java.util.Locale;

public class SettingsActivity extends Activity {
    private static final String[] DAYS = {"1 день", "2 дня", "3 дня", "4 дня", "5 дней", "6 дней", "7 дней", "8 дней", "9 дней", "10 дней"};
    int hour, minute;
    TextView timeText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton btnSettings = findViewById(R.id.footer__settings_button);
        btnSettings.setActivated(true);

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
}
