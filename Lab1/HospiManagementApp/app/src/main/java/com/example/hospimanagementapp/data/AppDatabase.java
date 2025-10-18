package com.example.hospimanagementapp.data; // Package for data-layer classes

import androidx.room.Database;          // Room annotation to define the DB schema
import androidx.room.Room;              // Factory for creating Room databases
import androidx.room.RoomDatabase;      // Base class for Room databases
import android.content.Context;         // Needed to build the DB with an app Context

import com.example.hospimanagementapp.data.PatientDao; // DAO for Patient operations
import com.example.hospimanagementapp.data.StaffDao;   // DAO for Staff operations
import com.example.hospimanagementapp.data.Patient; // Entity mapped to a table
import com.example.hospimanagementapp.data.Staff;   // Entity mapped to a table

@Database(entities = {Patient.class, Staff.class}, version = 1, exportSchema = false)
//  Declares the Room database: which entities it manages, the schema version,
//   and whether to export the schema as JSON for tooling (false = do not export).
public abstract class AppDatabase extends RoomDatabase { // Concrete DB extends RoomDatabase

    // Singleton instance (volatile ensures visibility across threads)
    private static volatile AppDatabase INSTANCE;

    // Room generates the implementation; these expose your DAOs to callers
    public abstract PatientDao patientDao();
    public abstract StaffDao staffDao();

    // Thread-safe double-checked locking to get/create the singleton DB
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) { // Fast path: already created?
            synchronized (AppDatabase.class) { // Serialise creation across threads
                if (INSTANCE == null) { // Second check inside the lock
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(), // Use app Context to avoid Activity leaks
                                    AppDatabase.class,               // The RoomDatabase subclass to create
                                    "hms_db"                         // On-device filename for the DB
                            )
                            .fallbackToDestructiveMigration()       // Wipes & rebuilds on version change if no migration (dev-friendly, data-loss risk)
                            .build();                               // Build the database instance
                }
            }
        }
        return INSTANCE; // Return the shared database
    }
}