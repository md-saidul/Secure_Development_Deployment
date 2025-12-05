package com.example.hospimanagementapp.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "vitals",
        indices = @Index("patientNHS")
)

public class Vitals {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String patientNHS;

    public float temperature;
    public int heartRate;
    public int glucose;
    public int oxygenLevel;

    public long timestamp;
    public boolean synced;
}
