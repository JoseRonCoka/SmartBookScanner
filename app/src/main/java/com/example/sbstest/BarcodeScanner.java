package com.example.sbstest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class BarcodeScanner {
    private final Activity activity;
    private final BarcodeScanCallback callback;

    public interface BarcodeScanCallback {
        void onBarcodeScanResult(String scannedResult);
    }

    public BarcodeScanner(Activity activity, BarcodeScanCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    public void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);

        // Launch the scanning activity
        // Pass the result back through the callback
        // Here, you would normally start the activity for result, but for simplicity, I'm just showing the callback
        // You would need to handle starting the activity and receiving the result accordingly
        String scannedResult = "Scanned result"; // Simulated scanned result
        callback.onBarcodeScanResult(scannedResult);
    }
}
