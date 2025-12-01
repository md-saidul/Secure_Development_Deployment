package com.example.hospimanagementapp.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.hospimanagementapp.R;
import com.example.hospimanagementapp.data.Appointment;
import com.example.hospimanagementapp.domain.GetTodaysAppointmentsUseCase;
import com.example.hospimanagementapp.security.RbacPolicyEvaluator;
import com.example.hospimanagementapp.ui.AppointmentAdapter;

import java.util.List;
import java.util.concurrent.Executors;

public class AppointmentListFragment extends Fragment {

    private Spinner spClinic;
    private ProgressBar progress;
    private androidx.recyclerview.widget.RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_appointment_list, container, false);
        spClinic = v.findViewById(R.id.spClinic);
        progress = v.findViewById(R.id.progress);
        rv = v.findViewById(R.id.rvAppointments);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayAdapter<String> clinics = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"All Clinics","Surgery A","Surgery B"}
        );
        spClinic.setAdapter(clinics);

        v.findViewById(R.id.btnRefresh).setOnClickListener(b -> loadData());

        v.findViewById(R.id.btnNewAppointment).setOnClickListener(b -> {
            if (!RbacPolicyEvaluator.canBookOrReschedule(requireContext())) {
                Toast.makeText(getContext(), "You are not permitted to book or reschedule appointments.", Toast.LENGTH_LONG).show();
                return;
            }

            Appointment newAppt = new Appointment();
            newAppt.id = 0L;
            newAppt.patientNhsNumber = "";

            long now = System.currentTimeMillis();

            newAppt.startTime = now;
            newAppt.endTime = now + 30 * 60 * 1000;
            newAppt.clinicianId = 0L;
            newAppt.clinicianName = "Unassigned";

            String clinic;
            int pos = spClinic.getSelectedItemPosition();
            if (pos <= 0) {
                clinic = "Surgery A";
            } else {
                clinic = spClinic.getSelectedItem().toString();
            }
            newAppt.clinic = clinic;

            newAppt.status = "BOOKED";

            BookingFragment f = BookingFragment.newInstance(newAppt);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.appointmentContainer, f).addToBackStack(null).commit();
        });

        loadData();
        return v;
    }

    private void loadData() {
        progress.setVisibility(View.VISIBLE);
        String clinic = spClinic.getSelectedItemPosition() == 0
                ? ""
                : spClinic.getSelectedItem().toString();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<Appointment> list = new GetTodaysAppointmentsUseCase(requireContext()).execute(clinic);

                requireActivity().runOnUiThread(() -> {
                    progress.setVisibility(View.GONE);
                    rv.setAdapter(new AppointmentAdapter(list, item -> {
                        if (!RbacPolicyEvaluator.canBookOrReschedule(requireContext())) {
                            Toast.makeText(getContext(), "You are not permitted to book or reschedule appointments.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        BookingFragment f = BookingFragment.newInstance(item);
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.appointmentContainer, f)
                                .addToBackStack(null)
                                .commit();
                    }));
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Failed to load. Please retry.", Toast.LENGTH_LONG).show();
                });
            }
        });
    }

}
