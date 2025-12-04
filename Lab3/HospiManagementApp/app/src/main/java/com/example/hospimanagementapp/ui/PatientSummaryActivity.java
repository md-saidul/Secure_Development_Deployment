package com.example.hospimanagementapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hospimanagementapp.R;
import com.example.hospimanagementapp.data.AppDatabase;
import com.example.hospimanagementapp.data.ClinicalRecord;

import java.util.concurrent.Executors;


public class PatientSummaryActivity extends AppCompatActivity {

    public static final String EXTRA_NHS = "nhsNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_summary);

        TextView tvPatientHeader = findViewById(R.id.tvPatientHeader);
        TextView tvAllergies = findViewById(R.id.tvAllergies);
        TextView tvConditions = findViewById(R.id.tvConditions);
        TextView tvMedications = findViewById(R.id.tvMedications);
        Button btnRecordVitals = findViewById(R.id.btnRecordVitals);

        String nhs = getIntent().getStringExtra(EXTRA_NHS);
        if (nhs == null) {
            nhs = "";
        }

        tvPatientHeader.setText("Patient NHS: " + nhs);

        String finalNhs = nhs;
        Executors.newSingleThreadExecutor().execute(() -> {
            ClinicalRecord record = AppDatabase.getInstance(getApplicationContext())
                    .clinicalRecordDao()
                    .findByPatient(finalNhs);

            runOnUiThread(() -> {
                if (record != null) {
                    tvConditions.setText("Conditions: " + safe(record.conditions));
                    tvAllergies.setText("Allergies: " + safe(record.allergies));
                    tvMedications.setText("Medications: " + safe(record.medications));;
                } else {
                    tvConditions.setText("Conditions: None");
                    tvAllergies.setText("Allergies: None");
                    tvMedications.setText("Medications: N/A");
                }
            });
        });

        String finalNhs1 = nhs;
        btnRecordVitals.setOnClickListener(v -> {
            Intent i = new Intent(this, VitalsActivity.class);
            i.putExtra(VitalsActivity.EXTRA_NHS, finalNhs);
            startActivity(i);
        });
    }

    private String safe(String s) {
        return s == null || s.trim().isEmpty()
                ? "-"
                : s;
    }
}