package com.example.hospimanagementapp.data;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ClinicalRecordDao {

    @Query("SELECT * FROM clinical_records WHERE patientNHS = :nhs LIMIT 1")
    ClinicalRecord findByPatient(String nhs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(ClinicalRecord record);
}
