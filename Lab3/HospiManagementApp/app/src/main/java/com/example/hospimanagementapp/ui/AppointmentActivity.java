package com.example.hospimanagementapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

import com.example.hospimanagementapp.R;
import com.example.hospimanagementapp.security.BiometricLoginCoordinator;
import com.example.hospimanagementapp.security.RbacPolicyEvaluator;

public class AppointmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        if (!RbacPolicyEvaluator.canViewAppointments(this)) {
            Toast.makeText(this, "Access denied. Please sign in with a permitted role.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        new BiometricLoginCoordinator().authenticate(this, new BiometricLoginCoordinator.Callback() {
            @Override
            public void onSuccess() {
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.appointmentContainer, new AppointmentListFragment())
                            .commit();
                }
            }
            @Override
            public void onFailure(String reason) {
                Toast.makeText(AppointmentActivity.this, "Biometric required: " + reason, Toast.LENGTH_LONG).show();

                Intent i = new Intent(AppointmentActivity.this, AdminLoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}