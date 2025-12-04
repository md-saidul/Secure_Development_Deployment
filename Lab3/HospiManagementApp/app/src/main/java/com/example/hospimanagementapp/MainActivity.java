package com.example.hospimanagementapp; // App's root package

import androidx.appcompat.app.AppCompatActivity; // Base class for modern Activities with ActionBar support

import android.content.Intent;  // Used to navigate between Activities
import android.os.Bundle;       // Holds saved instance state for lifecycle
import android.widget.Button;   // UI widget: Button
import android.widget.TextView; // UI widget: TextView
import android.widget.Toast;

import com.example.hospimanagementapp.ui.AdminLoginActivity;        // Screen for admin sign-in
import com.example.hospimanagementapp.ui.AppointmentActivity;
import com.example.hospimanagementapp.ui.PatientRegistrationActivity; // Screen to register patients
import com.example.hospimanagementapp.ui.PatientSummaryActivity;
import com.example.hospimanagementapp.util.SessionManager;          // Helper for simple session storage
import com.example.hospimanagementapp.ui.BarcodeScannerActivity;

public class MainActivity extends AppCompatActivity { // Entry Activity shown at app launch

    private TextView tvWelcome, tvOrText;       // Header showing session state
    private Button btnPatientRegistration, btnAdminPortal, btnLogout, btnAppointments, btnBarcode, btnPatientVitals; // Main menu buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Lifecycle: called when Activity is created
        super.onCreate(savedInstanceState);              // Always call the superclass first
        setContentView(R.layout.activity_main);          // Inflate the layout defined in activity_main.xml

        // Bind views from the layout to fields
        tvWelcome = findViewById(R.id.tvWelcome);
        tvOrText = findViewById(R.id.tvOrText);
        btnPatientRegistration = findViewById(R.id.btnPatientRegistration);
        btnAdminPortal = findViewById(R.id.btnAdminPortal);
        btnLogout = findViewById(R.id.btnLogout);
        btnAppointments = findViewById(R.id.btnAppointments);
        btnBarcode = findViewById(R.id.btnBarcode);
        btnPatientVitals = findViewById(R.id.btnPatientVitals);

        refreshHeader(); // Show current sign-in state immediately

        // Navigate to the Patient Registration screen
        btnPatientRegistration.setOnClickListener(v ->
                startActivity(new Intent(this, PatientRegistrationActivity.class)));

        // Navigate to Admin login first (RBAC gate). AdminPortal follows after successful login.
        btnAdminPortal.setOnClickListener(v -> {
            // RBAC gate: ADMIN only. If no admin exists yet, AdminLogin can handle bootstrap.
            Intent i = new Intent(this, AdminLoginActivity.class);
            startActivity(i);
        });

        btnAppointments.setOnClickListener(v -> {
            String role = SessionManager.getCurrentRole(this);
            if (role == null || role.isEmpty()) {
                Toast.makeText(this, "You are not logged in. Please log in first.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                startActivity(new Intent(this, AppointmentActivity.class));
            }
        });

        btnBarcode.setOnClickListener(v ->
            startActivity(new Intent(this, BarcodeScannerActivity.class)));

        btnPatientVitals.setOnClickListener(v -> {
            Intent i = new Intent(this, PatientSummaryActivity.class);
            i.putExtra(PatientSummaryActivity.EXTRA_NHS, "1234567890");
            startActivity(i);
        });

        // Clear session and update the header (acts like a simple "log out")
        btnLogout.setOnClickListener(v -> {
            SessionManager.clear(this); // Remove stored role/email
            refreshHeader();            // Reflect the logged-out state in the UI
        });
    }

    // Update the welcome header with the current session info
    private void refreshHeader() {
        String role = SessionManager.getCurrentRole(this);   // Read stored role (e.g., "ADMIN")
        String email = SessionManager.getCurrentEmail(this); // Read stored email
        if (role == null || role.isEmpty()) {                // No session present
            tvWelcome.setText("Welcome (not signed in)");    // Guest view
        } else {
            tvWelcome.setText("Signed in: " + email + " (" + role + ")"); // Show who is signed in
        }
    }

    @Override
    protected void onResume() { // Lifecycle: called when Activity comes to the foreground
        super.onResume();       // Always call the superclass
        refreshHeader();        // Re-read session in case it changed while away
    }
}