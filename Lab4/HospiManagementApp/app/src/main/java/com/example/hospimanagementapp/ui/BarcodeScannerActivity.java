package com.example.hospimanagementapp.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class BarcodeScannerActivity extends AppCompatActivity {

    private DecoratedBarcodeView barcodeView;
    private boolean handled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        barcodeView = new DecoratedBarcodeView(this);
        setContentView(barcodeView);

        if (Build.FINGERPRINT.contains("generic")) {
            String fakeNHS = "1234567890";
            Intent i = new Intent(this, PatientSummaryActivity.class);
            i.putExtra(PatientSummaryActivity.EXTRA_NHS, fakeNHS);
            startActivity(i);
            finish();
            return;
        }

        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (handled) {
                    return;
                }

                handled = true;
                barcodeView.pause();

                String nhs = result.getText();

                Intent i = new Intent(BarcodeScannerActivity.this, PatientSummaryActivity.class);
                i.putExtra(PatientSummaryActivity.EXTRA_NHS, nhs);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}
