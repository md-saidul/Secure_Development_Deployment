package com.example.hospimanagementapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hospimanagementapp.R;
import com.example.hospimanagementapp.data.AppDatabase;
import com.example.hospimanagementapp.data.ClinicalRecord;

import java.util.concurrent.Executors;

public class EditClinicalRecordActivity extends AppCompatActivity {

    private EditText etConditions, etAllergies, etMedications;
    private Button btnSave;

    private String nhs;
    private ClinicalRecord currentRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_clinical_record);

        etConditions = findViewById(R.id.etConditions);
        etAllergies = findViewById(R.id.etAllergies);
        etMedications = findViewById(R.id.etMedications);
        btnSave = findViewById(R.id.btnSave);

        nhs = getIntent().getStringExtra(PatientSummaryActivity.EXTRA_NHS);
        if (nhs == null) {
            nhs = "";
        }

        loadRecord();
        btnSave.setOnClickListener(v -> saveRecord());
    }

    private void loadRecord() {
        Executors.newSingleThreadExecutor().execute(() -> {
            currentRecord = AppDatabase.getInstance(getApplicationContext())
                    .clinicalRecordDao()
                    .findByPatient(nhs);

            runOnUiThread(() -> {
                if (currentRecord != null) {
                    etConditions.setText(currentRecord.conditions);
                    etAllergies.setText(currentRecord.allergies);
                    etMedications.setText(currentRecord.medications);
                }
            });
        });
    }

    private void saveRecord() {
        String conditions = etConditions.getText().toString().trim();
        String allergies = etAllergies.getText().toString().trim();
        String medications = etMedications.getText().toString().trim();

        if (TextUtils.isEmpty(conditions) &&
                TextUtils.isEmpty(allergies) &&
                TextUtils.isEmpty(medications)) {
            Toast.makeText(this, "Please fill in at least one field", Toast.LENGTH_SHORT).show();
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            ClinicalRecord record = currentRecord != null
                    ? currentRecord
                    : new ClinicalRecord();

            record.patientNHS = nhs;
            record.conditions = conditions;
            record.allergies = allergies;
            record.medications = medications;
            record.updatedAt = System.currentTimeMillis();

            AppDatabase.getInstance(getApplicationContext())
                    .clinicalRecordDao()
                    .upsert(record);

            runOnUiThread(() -> {
                Toast.makeText(this, "Record Saved", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
