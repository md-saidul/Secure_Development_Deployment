package com.example.hospimanagementapp.domain;

import android.content.Context;

import com.example.hospimanagementapp.data.Appointment;
import com.example.hospimanagementapp.data.AppointmentRepository;

import java.util.List;

public class DetectScheduleConflictsUseCase {
    private final AppointmentRepository repo;

    public DetectScheduleConflictsUseCase(Context ctx) {
        this.repo = new AppointmentRepository(ctx);
    }

    public boolean hasConflict(long clinicianId, long start, long end) {
        List<Appointment> overlaps = repo.detectConflicts(clinicianId, start, end);
        return overlaps != null && !overlaps.isEmpty();
    }
}
