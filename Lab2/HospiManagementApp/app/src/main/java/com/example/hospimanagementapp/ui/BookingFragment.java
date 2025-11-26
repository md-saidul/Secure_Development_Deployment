package com.example.hospimanagementapp.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.hospimanagementapp.R;
import com.example.hospimanagementapp.data.AppDatabase;
import com.example.hospimanagementapp.data.Appointment;
import com.example.hospimanagementapp.domain.BookOrRescheduleAppointmentUseCase;
import com.example.hospimanagementapp.domain.DetectScheduleConflictsUseCase;
import com.example.hospimanagementapp.security.RbacPolicyEvaluator;
import com.example.hospimanagementapp.util.ValidationUtils;

import java.util.concurrent.Executors;

public class BookingFragment extends Fragment {
    private static final String ARG_ID = "id";
    private static final String ARG_CLINICIAN_ID = "clinicianId";
    private static final String ARG_CLINICIAN_NAME = "clinicianName";
    private static final String ARG_PATIENT_NHS = "patientNhs";
    private static final String ARG_START = "start";
    private static final String ARG_END = "end";
    private static final String ARG_CLINIC = "clinic";


    public static BookingFragment newInstance(Appointment a) {
        Bundle b = new Bundle();
        b.putLong(ARG_ID, a.id);
        b.putLong(ARG_CLINICIAN_ID, a.clinicianId);
        b.putString(ARG_CLINICIAN_NAME, a.clinicianName);
        b.putString(ARG_PATIENT_NHS, a.patientNhsNumber);
        b.putLong(ARG_START, a.startTime);
        b.putLong(ARG_END, a.endTime);
        b.putString(ARG_CLINIC, a.clinic);
        BookingFragment f = new BookingFragment();
        f.setArguments(b);
        return f;
    }

    private EditText etStart, etEnd, etNhs;
    private TextView tvClinician;
    private Button btnConfirm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_booking, container, false);

        tvClinician = v.findViewById(R.id.tvClinician);
        etNhs = v.findViewById(R.id.etNhsBooking);
        etStart = v.findViewById(R.id.etStartMillis);
        etEnd = v.findViewById(R.id.etEndMillis);
        btnConfirm = v.findViewById(R.id.btnConfirmBooking);

        Bundle args = getArguments();
        if (args != null) {
            tvClinician.setText("Clinician: " + args.getString(ARG_CLINICIAN_NAME, ""));
            etNhs.setText(args.getString(ARG_PATIENT_NHS, ""));
            etStart.setText(String.valueOf(args.getLong(ARG_START)));
            etEnd.setText(String.valueOf(args.getLong(ARG_END)));
        }

        btnConfirm.setOnClickListener(v1 -> confirm());
        return v;
    }

    private void confirm() {
        if (!RbacPolicyEvaluator.canBookOrReschedule(requireContext())) {
            Toast.makeText(getContext(), "You do not have permission to book.", Toast.LENGTH_LONG).show();
            return;
        }

        String nhs = etNhs.getText().toString().trim();
        String startStr = etStart.getText().toString().trim();
        String endStr = etEnd.getText().toString().trim();

        if (TextUtils.isEmpty(nhs) || TextUtils.isEmpty(startStr) || TextUtils.isEmpty(endStr)) {
            Toast.makeText(getContext(), "NHS number, start and end are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        long clinicianId = getArguments().getLong(ARG_CLINICIAN_ID);
        long start;
        long end;

        try {
            start = Long.parseLong(startStr);
            end = Long.parseLong(endStr);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Start and end times must valid.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (end <= start) {
            Toast.makeText(getContext(), "End time must be after start time.", Toast.LENGTH_SHORT).show();
            return;
        }

        String clinic = getArguments().getString(ARG_CLINIC);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                int count = AppDatabase.getInstance(requireContext()).patientDao().countByNhs(nhs);

                if (count == 0) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Patient not found.", Toast.LENGTH_LONG).show());
                    return;
                }

                // conflict detection
                boolean conflict = new DetectScheduleConflictsUseCase(requireContext()).hasConflict(clinicianId, start, end);
                if (conflict) {
                    Toast.makeText(getContext(), "Time conflict detected. Choose another slot.", Toast.LENGTH_LONG).show();
                    return;
                }

                Appointment a = new Appointment();
                a.id = getArguments().getLong(ARG_ID, 0L);
                a.patientNhsNumber = nhs;
                a.clinicianId = clinicianId;
                a.clinicianName = getArguments().getString(ARG_CLINICIAN_NAME);
                a.startTime = start;
                a.endTime = end;
                a.clinic = clinic;
                a.status = "BOOKED";

                new BookOrRescheduleAppointmentUseCase(requireContext()).execute(a);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Appointment booked.", Toast.LENGTH_LONG).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Booking failed. Try again." + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }
}
