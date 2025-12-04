package com.example.hospimanagementapp.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "clinical_records",
        indices = @Index(value = "patientNHS", unique = true)
)

public class ClinicalRecord {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String patientNHS;

    public  String allergies;
    public String medications;
    public String conditions;

    public long updatedAt;
}
