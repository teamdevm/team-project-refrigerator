package com.pmi.kysp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageButton btnSettings = findViewById(R.id.footer__settings_button);
        btnSettings.setActivated(true);
    }
}
