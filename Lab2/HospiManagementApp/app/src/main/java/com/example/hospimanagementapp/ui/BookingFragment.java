package com.example.hospimanagementapp.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.example.hospimanagementapp.data.Staff;
import com.example.hospimanagementapp.domain.BookOrRescheduleAppointmentUseCase;
import com.example.hospimanagementapp.domain.DetectScheduleConflictsUseCase;
import com.example.hospimanagementapp.security.RbacPolicyEvaluator;
import com.example.hospimanagementapp.util.ValidationUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;

public class BookingFragment extends Fragment {
    private static final String ARG_ID = "id";
    private static final String ARG_PATIENT_NHS = "nhs";
    private static final String ARG_START = "start";
    private static final String ARG_END = "end";
    private static final String ARG_CLINICIAN_ID = "clnId";

    private final SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.UK);

    public static BookingFragment newInstance(Appointment a) {
        Bundle b = new Bundle();
        b.putLong(ARG_ID, a.id);
        b.putString(ARG_PATIENT_NHS, a.patientNhsNumber);
        b.putLong(ARG_START, a.startTime);
        b.putLong(ARG_END, a.endTime);
        b.putLong(ARG_CLINICIAN_ID, a.clinicianId);

        BookingFragment f = new BookingFragment();
        f.setArguments(b);
        return f;
    }

    private EditText etNhsBooking;
    private TextView tvClinician, tvClinic, tvNHS, tvStartTime, tvEndTime;
    private Spinner spClinician;
    private Button btnConfirm, btnStartDate, btnStartTime, btnEndTime;;

    private long startMillis = 0;
    private long endMillis = 0;

    private List<Staff> clinicians;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_booking, container, false);

        etNhsBooking = v.findViewById(R.id.etNhsBooking);
        tvClinician = v.findViewById(R.id.tvClinician);
        tvClinic = v.findViewById(R.id.tvClinic);
        tvNHS = v.findViewById(R.id.tvNHS);
        tvStartTime = v.findViewById(R.id.tvStartTime);
        tvEndTime = v.findViewById(R.id.tvEndTime);
        spClinician = v.findViewById(R.id.spClinician);
        btnConfirm = v.findViewById(R.id.btnConfirmBooking);
        btnStartDate = v.findViewById(R.id.btnStartDate);
        btnStartTime = v.findViewById(R.id.btnStartTime);
        btnEndTime = v.findViewById(R.id.btnEndTime);



        Bundle args = getArguments();
        String existingClinicianName = "";
        long existingStart = 0L;
        long existingEnd = 0L;
        String existingNhs = "";


        if (!TextUtils.isEmpty(existingClinicianName)
                && !"Unassigned".equalsIgnoreCase(existingClinicianName)) {
            tvClinician.setText("Clinician (current): " + existingClinicianName);
        } else {
            tvClinician.setText("Clinician: (select below)");
        }

        loadClinicians();
        applyExistingValues();

        btnStartDate.setOnClickListener(v1 -> pickStartDate());
        btnStartTime.setOnClickListener(v1 -> pickStartTime());
        btnEndTime.setOnClickListener(v1 -> pickEndTime());
        btnConfirm.setOnClickListener(v1 -> confirm());

        return v;
    }

    private void applyExistingValues() {
        Bundle args = getArguments();
        if (args == null) return;

        etNhsBooking.setText(args.getString(ARG_PATIENT_NHS, ""));

        startMillis = args.getLong(ARG_START, 0);
        endMillis = args.getLong(ARG_END, 0);

        updateDisplayedTimes();
    }

    private void loadClinicians() {
        Executors.newSingleThreadExecutor().execute(() -> {
            clinicians = AppDatabase.getInstance(requireContext())
                    .staffDao()
                    .getClinicians();

            requireActivity().runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_dropdown_item
                );

                for (Staff s : clinicians)
                    adapter.add(s.fullName + " (" + s.clinic + ")");

                spClinician.setAdapter(adapter);

                spClinician.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView < ? > parent, View view, int pos, long id) {
                        Staff selected = clinicians.get(pos);
                        tvClinic.setText("Clinic: " + selected.clinic);
                    }

                    @Override
                    public void onNothingSelected(AdapterView < ? > parent) {}
                });

                long existingId = getArguments().getLong(ARG_CLINICIAN_ID, -1);
                if (existingId > 0) {
                    for (int i = 0; i < clinicians.size(); i++) {
                        if (clinicians.get(i).id == existingId) {
                            spClinician.setSelection(i);
                            break;
                        }
                    }
                }
            });
        });
    }


    private void pickStartDate() {
        final Calendar c = Calendar.getInstance();

        new DatePickerDialog(requireContext(), (picker, y, m, d) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(y, m, d);

            if (startMillis > 0) {
                Calendar old = Calendar.getInstance();
                old.setTimeInMillis(startMillis);
                cal.set(Calendar.HOUR_OF_DAY, old.get(Calendar.HOUR_OF_DAY));
                cal.set(Calendar.MINUTE, old.get(Calendar.MINUTE));
            }

            startMillis = cal.getTimeInMillis();

            if (endMillis == 0)
                endMillis = startMillis + 15 * 60 * 1000;

            updateDisplayedTimes();
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }


    private void pickStartTime() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = 0;

        new TimePickerDialog(requireContext(), (picker, h, m) -> {
            Calendar cal = Calendar.getInstance();
            if (startMillis > 0) cal.setTimeInMillis(startMillis);

            cal.set(Calendar.HOUR_OF_DAY, h);
            cal.set(Calendar.MINUTE, m);
            startMillis = cal.getTimeInMillis();
            endMillis = startMillis + 15 * 60 * 1000;

            updateDisplayedTimes();
        }, hour, minute, true).show();
    }


    private void pickEndTime() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = 0;

        new TimePickerDialog(requireContext(), (picker, h, m) -> {
            if (startMillis == 0) {
                Toast.makeText(requireContext(), "Pick start time.", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(startMillis);
            cal.set(Calendar.HOUR_OF_DAY, h);
            cal.set(Calendar.MINUTE, m);
            endMillis = cal.getTimeInMillis();

            updateDisplayedTimes();
        }, hour, minute, true).show();
    }


    private void updateDisplayedTimes() {
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.UK);

        tvStartTime.setText(startMillis == 0 ? "(none)" : displayFormat.format(startMillis));
        tvEndTime.setText(endMillis == 0 ? "(none)" : displayFormat.format(endMillis));
    }



    private void showDateTimePicker(EditText target) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog dpDialog = new DatePickerDialog(
                requireContext(),
                (view, year, month, day) -> {
                    TimePickerDialog tpDialog = new TimePickerDialog(
                            requireContext(),
                            (timepicker, hour, minute) -> {
                                Calendar chosen = Calendar.getInstance();
                                chosen.set(year, month, day, hour, minute, 0);
                                target.setText(displayFormat.format(chosen.getTime()));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                    );
                    tpDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dpDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dpDialog.show();
    }

    private void confirm() {
        if (!RbacPolicyEvaluator.canBookOrReschedule(requireContext())) {
            Toast.makeText(getContext(),
                    "You do not have permission to book.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String nhs = etNhsBooking.getText().toString().trim();
        if (TextUtils.isEmpty(nhs) || startMillis == 0 || endMillis == 0) {
            Toast.makeText(getContext(), "NHS, Start Time and End Time are required.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(nhs)) {
            Toast.makeText(getContext(),
                    "NHS number, start time and end time are required.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Staff selectedClinician = clinicians.get(spClinician.getSelectedItemPosition());

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                int count = AppDatabase.getInstance(requireContext())
                        .patientDao()
                        .countByNhs(nhs);

                if (count == 0) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(),
                                    "Patient not found. Please register the patient first.",
                                    Toast.LENGTH_LONG).show());
                    return;
                }

                boolean conflict = new DetectScheduleConflictsUseCase(requireContext())
                        .hasConflict(selectedClinician.id, startMillis, endMillis);

                if (conflict) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(),
                                    "Time conflict. Choose another slot.",
                                    Toast.LENGTH_LONG).show());
                    return;
                }

                Appointment a = new Appointment();
                a.id = getArguments().getLong(ARG_ID, 0L);
                a.patientNhsNumber = nhs;
                a.clinicianId = selectedClinician.id;
                a.clinicianName = selectedClinician.fullName;
                a.startTime = startMillis;
                a.endTime = endMillis;
                a.clinic = selectedClinician.clinic;
                a.status = "BOOKED";

                new BookOrRescheduleAppointmentUseCase(requireContext()).execute(a);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(),
                            "Appointment confirmed.",
                            Toast.LENGTH_LONG).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(),
                                "Booking failed. Try again.",
                                Toast.LENGTH_LONG).show());
            }
        });
    }
}
