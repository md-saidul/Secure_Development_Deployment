package com.example.hospimanagementapp.network;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.hospimanagementapp.data.AppDatabase;
import com.example.hospimanagementapp.data.Vitals;

import java.util.List;

public class VitalsSyncWorker extends Worker {

    public VitalsSyncWorker(@NonNull Context context,
                            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Offline sync
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        List<Vitals> pending = db.vitalsDao().getPending();

        for (Vitals v : pending) {
            db.vitalsDao().markSynced(v.id);
        }

        return Result.success();
    }
}
