package com.pmi.kysp;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BarcodeScanner {
    private static Toast toast;

    public static void Scan(Activity activity) {

        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        intentIntegrator.setPrompt("Отсканируйте штрих-код продукта");
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();
        toast = Toast.makeText(activity, "Вкл/Выкл фонарика кнопками громкости", Toast.LENGTH_LONG);
        toast.show();
    }

    public static String Decode(int requestCode, int resultCode, @Nullable Intent data){
        toast.cancel();
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
