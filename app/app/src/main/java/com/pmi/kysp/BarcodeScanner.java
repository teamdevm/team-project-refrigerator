package com.pmi.kysp;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeScanner {

    public static void Scan(Activity activity) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        intentIntegrator.setPrompt("Вкл/Выкл фонарика кнопками громкости");
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();
    }

    public static String Decode(int requestCode, int resultCode, @Nullable Intent data){
        IntentResult intentResult = IntentIntegrator.parseActivityResult(
                requestCode, resultCode, data
        );
        String content = intentResult.getContents();
        return content == null || !CheckBarcode(content) ? null : content;
    }

    private static boolean CheckBarcode(String code){
        for (int i = 0; i < code.length(); ++i) {
            if (code.charAt(i) < '0' || code.charAt(i) > '9') return false;
        }
        return true;
    }
}
