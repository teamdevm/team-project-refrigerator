package com.pmi.kysp;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class Capture extends CaptureActivity {
    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.activity_camera);
        return (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
    }
}
