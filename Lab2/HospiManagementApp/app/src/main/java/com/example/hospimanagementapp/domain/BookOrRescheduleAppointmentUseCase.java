package com.example.hospimanagementapp.domain;

import android.content.Context;

import com.example.hospimanagementapp.data.Appointment;
import com.example.hospimanagementapp.data.AppointmentRepository;

public class BookOrRescheduleAppointmentUseCase {
    private final AppointmentRepository repo;

    public BookOrRescheduleAppointmentUseCase(Context ctx) {
        this.repo = new AppointmentRepository(ctx);
    }

    public Appointment execute(Appointment appt) throws Exception {
        return repo.bookOrReschedule(appt);
    }
}
