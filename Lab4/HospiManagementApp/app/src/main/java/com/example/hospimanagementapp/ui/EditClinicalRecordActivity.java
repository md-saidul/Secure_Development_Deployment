package com.example.hospimanagementapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hospimanagementapp.R;
import com.example.hospimanagementapp.data.AppDatabase;
import com.example.hospimanagementapp.data.ClinicalRecord;
import com.example.hospimanagementapp.util.CryptoManager;

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
                    String conditions = CryptoManager.decrypt(currentRecord.conditions);
                    String allergies = CryptoManager.decrypt(currentRecord.allergies);
                    String medications = CryptoManager.decrypt(currentRecord.medications);

                    etConditions.setText(conditions);
                    etAllergies.setText(allergies);
                    etMedications.setText(medications);
                } else {
                    etConditions.setText("");
                    etAllergies.setText("");
                    etMedications.setText("");
                }
            });
        });
    }

    private void saveRecord() {
        String conditions = etConditions.getText().toString().trim();
        String allergies = etAllergies.getText().toString().trim();
        String medications = etMedications.getText().toString().trim();

        if (TextUtils.isEmpty(conditions)
                && TextUtils.isEmpty(allergies)
                && TextUtils.isEmpty(medications)) {
            Toast.makeText(this, "Please fill in at least one field", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSave.setEnabled(false);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());

                if (currentRecord == null) {
                    currentRecord = new ClinicalRecord();
                    currentRecord.patientNHS = nhs;
                }

                Log.d("ENCRYPT_TEST", "Encrypted conditions = " + CryptoManager.encrypt(conditions));

                currentRecord.conditions = CryptoManager.encrypt(conditions);
                currentRecord.allergies = CryptoManager.encrypt(allergies);
                currentRecord.medications = CryptoManager.encrypt(medications);
                currentRecord.updatedAt = System.currentTimeMillis();

                db.clinicalRecordDao().upsert(currentRecord);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Record Saved", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    btnSave.setEnabled(true);
                    Toast.makeText(this, "Error saving record", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
