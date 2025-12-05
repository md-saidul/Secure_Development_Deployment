package com.example.hospimanagementapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VitalsDao {

    @Query("SELECT * FROM vitals WHERE synced = 0")
    List<Vitals> getPending();

    @Insert
    long insert(Vitals v);

    @Query("UPDATE vitals SET synced = 1 WHERE id = :id")
    void markSynced(long id);

    @Query("SELECT COUNT(*) FROM vitals WHERE patientNHS = :nhs")
    int countForPatient(String nhs);

    @Query("SELECT * FROM vitals " +
            "WHERE patientNHS = :nhs " +
            "ORDER BY timestamp DESC " +
            "LIMIT :limit OFFSET :offset")
    List<Vitals> pageForPatient(String nhs, int limit, int offset);
}
