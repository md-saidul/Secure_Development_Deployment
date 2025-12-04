package com.example.hospimanagementapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
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

    private TextView tvAllergies, tvConditions, tvMedications;
    private String finalNhs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_summary);

        TextView tvPatientHeader = findViewById(R.id.tvPatientHeader);
        LinearLayout recordContainer = findViewById(R.id.recordContainer);
        tvAllergies = findViewById(R.id.tvAllergies);
        tvConditions = findViewById(R.id.tvConditions);
        tvMedications = findViewById(R.id.tvMedications);
        Button btnRecordVitals = findViewById(R.id.btnRecordVitals);


        String nhs = getIntent().getStringExtra(EXTRA_NHS);
        if (nhs == null) {
            nhs = "";
        }

        finalNhs = nhs;

        tvPatientHeader.setText("Patient NHS: " + nhs);

        recordContainer.setOnClickListener(v -> {
            Intent i = new Intent(this, EditClinicalRecordActivity.class);
            i.putExtra(EXTRA_NHS, finalNhs);
            startActivity(i);
        });

        btnRecordVitals.setOnClickListener(v -> {
            Intent i = new Intent(this, VitalsActivity.class);
            i.putExtra(VitalsActivity.EXTRA_NHS, finalNhs);
            startActivity(i);
        });

        loadRecord();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecord();
    }

    private void loadRecord() {
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
    }

    private String safe(String s) {
        return s == null || s.trim().isEmpty()
                ? "-"
                : s;
    }
}