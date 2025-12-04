package com.example.hospimanagementapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.hospimanagementapp.R;
import com.example.hospimanagementapp.data.AppDatabase;
import com.example.hospimanagementapp.data.Vitals;
import com.example.hospimanagementapp.network.VitalsSyncWorker;

import java.util.List;
import java.util.concurrent.Executors;

public class VitalsActivity extends AppCompatActivity {

    public static final String EXTRA_NHS = "nhsNumber";

    private TextView tvVitalsHeader, tvPageInfo;
    private EditText etTemperature, etHeartRate, etGlucose, etOxygenLevel;
    private Button btnSaveVitals, btnPreviousPage, btnNextPage;
    private VitalsAdapter adapter;
    private RecyclerView rv;

    private String nhs;
    private int page = 1;
    private final int pageSize = 10;
    private int totalPages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vitals);

        nhs = getIntent().getStringExtra(EXTRA_NHS);
        if (nhs == null) {
            nhs = "";
        }

        tvVitalsHeader = findViewById(R.id.tvVitalsHeader);
        tvVitalsHeader.setText("Vitals for NHS: " + nhs);

        etTemperature = findViewById(R.id.etTemperature);
        etHeartRate = findViewById(R.id.etHeartRate);
        etGlucose = findViewById(R.id.etGlucose);
        etOxygenLevel = findViewById(R.id.etOxygenLevel);
        btnSaveVitals = findViewById(R.id.btnSaveVitals);
        btnPreviousPage = findViewById(R.id.btnPreviousPage);
        btnNextPage = findViewById(R.id.btnNextPage);

        tvPageInfo = findViewById(R.id.tvPageInfo);
        rv = findViewById(R.id.rvVitals);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new VitalsAdapter();
        rv.setAdapter(adapter);

        btnSaveVitals.setOnClickListener(v -> saveVitals());
        btnPreviousPage.setOnClickListener(v -> {
            if (page > 1) {
                page--;
                loadPage();
            }
        });
        btnNextPage.setOnClickListener(v -> {
            if (page < totalPages) {
                page++;
                loadPage();
            }
        });

        loadPage();
    }

    private void saveVitals() {
        String tempString = etTemperature.getText().toString().trim();
        String hrString = etHeartRate.getText().toString().trim();
        String gluString = etGlucose.getText().toString().trim();
        String oxyString = etOxygenLevel.getText().toString().trim();

        if (TextUtils.isEmpty(tempString) ||
                TextUtils.isEmpty(hrString) ||
                TextUtils.isEmpty(gluString) ||
                TextUtils.isEmpty(oxyString)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        float temp;
        int hr, oxy;
        float glucose = 0f;

        try {
            temp = Float.parseFloat(tempString);
            hr = Integer.parseInt(hrString);
            oxy = Integer.parseInt(oxyString);

            if (!TextUtils.isEmpty(gluString)) {
                glucose = Float.parseFloat(gluString);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid format", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSaveVitals.setEnabled(false);

        float finalTemp = temp;
        int finalHr = hr;
        float finalGlucose = glucose;
        int finalOxy = oxy;

        Executors.newSingleThreadExecutor().execute(() -> {
            long now = System.currentTimeMillis();

            Vitals v = new Vitals();
            v.patientNHS = nhs;
            v.temperature = finalTemp;
            v.heartRate = finalHr;
            v.glucose = (int) finalGlucose;
            v.oxygenLevel = finalOxy;
            v.timestamp = now;
            v.synced = false;

            AppDatabase.getInstance(getApplicationContext()).vitalsDao().insert(v);

            OneTimeWorkRequest req = new OneTimeWorkRequest.Builder(VitalsSyncWorker.class).build();
            WorkManager.getInstance(getApplicationContext()).enqueue(req);

            runOnUiThread(() -> {
                btnSaveVitals.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Vitals saved", Toast.LENGTH_SHORT).show();
                etTemperature.setText("");
                etHeartRate.setText("");
                etGlucose.setText("");
                etOxygenLevel.setText("");
                page = 1;
                loadPage();
            });
        });
    }

    private void loadPage() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            int count = db.vitalsDao().countForPatient(nhs);
            totalPages = Math.max(1, (int) Math.ceil(count / (double) pageSize));
            if (page > totalPages) {
                page = totalPages;
            }
            if (page < 1) {
                page = 1;
            }

            int offset = (page - 1) * pageSize;
            List<Vitals> list = db.vitalsDao().pageForPatient(nhs, pageSize, offset);

            runOnUiThread(() -> {
                adapter.submit(list);
                tvPageInfo.setText("Page " + page + " of " + totalPages);
                btnPreviousPage.setEnabled(page > 1);
                btnNextPage.setEnabled(page < totalPages);
            });
        });
    }
}