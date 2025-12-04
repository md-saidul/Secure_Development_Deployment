package com.example.hospimanagementapp.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile PatientDao _patientDao;

  private volatile StaffDao _staffDao;

  private volatile AppointmentDao _appointmentDao;

  private volatile ClinicalRecordDao _clinicalRecordDao;

  private volatile VitalsDao _vitalsDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `patients` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nhsNumber` TEXT NOT NULL, `fullName` TEXT, `dateOfBirth` TEXT, `phone` TEXT, `email` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_patients_nhsNumber` ON `patients` (`nhsNumber`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `staff` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `fullName` TEXT, `email` TEXT NOT NULL, `role` TEXT NOT NULL, `adminPin` TEXT, `clinic` TEXT)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_staff_email` ON `staff` (`email`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `appointments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patientNhsNumber` TEXT, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `clinicianId` INTEGER NOT NULL, `clinicianName` TEXT, `clinic` TEXT, `status` TEXT)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_appointments_startTime_clinicianId` ON `appointments` (`startTime`, `clinicianId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `clinical_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patientNHS` TEXT NOT NULL, `allergies` TEXT, `medications` TEXT, `conditions` TEXT, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_clinical_records_patientNHS` ON `clinical_records` (`patientNHS`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `vitals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `patientNHS` TEXT NOT NULL, `temperature` REAL NOT NULL, `heartRate` INTEGER NOT NULL, `glucose` INTEGER NOT NULL, `oxygenLevel` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, `synced` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_vitals_patientNHS` ON `vitals` (`patientNHS`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2ba8308f393ba50e64f116c1b276c0ce')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `patients`");
        db.execSQL("DROP TABLE IF EXISTS `staff`");
        db.execSQL("DROP TABLE IF EXISTS `appointments`");
        db.execSQL("DROP TABLE IF EXISTS `clinical_records`");
        db.execSQL("DROP TABLE IF EXISTS `vitals`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPatients = new HashMap<String, TableInfo.Column>(8);
        _columnsPatients.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("nhsNumber", new TableInfo.Column("nhsNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("fullName", new TableInfo.Column("fullName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("dateOfBirth", new TableInfo.Column("dateOfBirth", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPatients = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPatients = new HashSet<TableInfo.Index>(1);
        _indicesPatients.add(new TableInfo.Index("index_patients_nhsNumber", true, Arrays.asList("nhsNumber"), Arrays.asList("ASC")));
        final TableInfo _infoPatients = new TableInfo("patients", _columnsPatients, _foreignKeysPatients, _indicesPatients);
        final TableInfo _existingPatients = TableInfo.read(db, "patients");
        if (!_infoPatients.equals(_existingPatients)) {
          return new RoomOpenHelper.ValidationResult(false, "patients(com.example.hospimanagementapp.data.Patient).\n"
                  + " Expected:\n" + _infoPatients + "\n"
                  + " Found:\n" + _existingPatients);
        }
        final HashMap<String, TableInfo.Column> _columnsStaff = new HashMap<String, TableInfo.Column>(6);
        _columnsStaff.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStaff.put("fullName", new TableInfo.Column("fullName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStaff.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStaff.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStaff.put("adminPin", new TableInfo.Column("adminPin", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStaff.put("clinic", new TableInfo.Column("clinic", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStaff = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStaff = new HashSet<TableInfo.Index>(1);
        _indicesStaff.add(new TableInfo.Index("index_staff_email", true, Arrays.asList("email"), Arrays.asList("ASC")));
        final TableInfo _infoStaff = new TableInfo("staff", _columnsStaff, _foreignKeysStaff, _indicesStaff);
        final TableInfo _existingStaff = TableInfo.read(db, "staff");
        if (!_infoStaff.equals(_existingStaff)) {
          return new RoomOpenHelper.ValidationResult(false, "staff(com.example.hospimanagementapp.data.Staff).\n"
                  + " Expected:\n" + _infoStaff + "\n"
                  + " Found:\n" + _existingStaff);
        }
        final HashMap<String, TableInfo.Column> _columnsAppointments = new HashMap<String, TableInfo.Column>(8);
        _columnsAppointments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppointments.put("patientNhsNumber", new TableInfo.Column("patientNhsNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppointments.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppointments.put("endTime", new TableInfo.Column("endTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppointments.put("clinicianId", new TableInfo.Column("clinicianId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppointments.put("clinicianName", new TableInfo.Column("clinicianName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppointments.put("clinic", new TableInfo.Column("clinic", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppointments.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppointments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAppointments = new HashSet<TableInfo.Index>(1);
        _indicesAppointments.add(new TableInfo.Index("index_appointments_startTime_clinicianId", false, Arrays.asList("startTime", "clinicianId"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoAppointments = new TableInfo("appointments", _columnsAppointments, _foreignKeysAppointments, _indicesAppointments);
        final TableInfo _existingAppointments = TableInfo.read(db, "appointments");
        if (!_infoAppointments.equals(_existingAppointments)) {
          return new RoomOpenHelper.ValidationResult(false, "appointments(com.example.hospimanagementapp.data.Appointment).\n"
                  + " Expected:\n" + _infoAppointments + "\n"
                  + " Found:\n" + _existingAppointments);
        }
        final HashMap<String, TableInfo.Column> _columnsClinicalRecords = new HashMap<String, TableInfo.Column>(6);
        _columnsClinicalRecords.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClinicalRecords.put("patientNHS", new TableInfo.Column("patientNHS", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClinicalRecords.put("allergies", new TableInfo.Column("allergies", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClinicalRecords.put("medications", new TableInfo.Column("medications", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClinicalRecords.put("conditions", new TableInfo.Column("conditions", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClinicalRecords.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysClinicalRecords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesClinicalRecords = new HashSet<TableInfo.Index>(1);
        _indicesClinicalRecords.add(new TableInfo.Index("index_clinical_records_patientNHS", true, Arrays.asList("patientNHS"), Arrays.asList("ASC")));
        final TableInfo _infoClinicalRecords = new TableInfo("clinical_records", _columnsClinicalRecords, _foreignKeysClinicalRecords, _indicesClinicalRecords);
        final TableInfo _existingClinicalRecords = TableInfo.read(db, "clinical_records");
        if (!_infoClinicalRecords.equals(_existingClinicalRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "clinical_records(com.example.hospimanagementapp.data.ClinicalRecord).\n"
                  + " Expected:\n" + _infoClinicalRecords + "\n"
                  + " Found:\n" + _existingClinicalRecords);
        }
        final HashMap<String, TableInfo.Column> _columnsVitals = new HashMap<String, TableInfo.Column>(8);
        _columnsVitals.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitals.put("patientNHS", new TableInfo.Column("patientNHS", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitals.put("temperature", new TableInfo.Column("temperature", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitals.put("heartRate", new TableInfo.Column("heartRate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitals.put("glucose", new TableInfo.Column("glucose", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitals.put("oxygenLevel", new TableInfo.Column("oxygenLevel", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitals.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVitals.put("synced", new TableInfo.Column("synced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVitals = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVitals = new HashSet<TableInfo.Index>(1);
        _indicesVitals.add(new TableInfo.Index("index_vitals_patientNHS", false, Arrays.asList("patientNHS"), Arrays.asList("ASC")));
        final TableInfo _infoVitals = new TableInfo("vitals", _columnsVitals, _foreignKeysVitals, _indicesVitals);
        final TableInfo _existingVitals = TableInfo.read(db, "vitals");
        if (!_infoVitals.equals(_existingVitals)) {
          return new RoomOpenHelper.ValidationResult(false, "vitals(com.example.hospimanagementapp.data.Vitals).\n"
                  + " Expected:\n" + _infoVitals + "\n"
                  + " Found:\n" + _existingVitals);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2ba8308f393ba50e64f116c1b276c0ce", "79c62cacaf17aa3675ba003c8551448a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "patients","staff","appointments","clinical_records","vitals");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `patients`");
      _db.execSQL("DELETE FROM `staff`");
      _db.execSQL("DELETE FROM `appointments`");
      _db.execSQL("DELETE FROM `clinical_records`");
      _db.execSQL("DELETE FROM `vitals`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PatientDao.class, PatientDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StaffDao.class, StaffDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AppointmentDao.class, AppointmentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ClinicalRecordDao.class, ClinicalRecordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VitalsDao.class, VitalsDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PatientDao patientDao() {
    if (_patientDao != null) {
      return _patientDao;
    } else {
      synchronized(this) {
        if(_patientDao == null) {
          _patientDao = new PatientDao_Impl(this);
        }
        return _patientDao;
      }
    }
  }

  @Override
  public StaffDao staffDao() {
    if (_staffDao != null) {
      return _staffDao;
    } else {
      synchronized(this) {
        if(_staffDao == null) {
          _staffDao = new StaffDao_Impl(this);
        }
        return _staffDao;
      }
    }
  }

  @Override
  public AppointmentDao appointmentDao() {
    if (_appointmentDao != null) {
      return _appointmentDao;
    } else {
      synchronized(this) {
        if(_appointmentDao == null) {
          _appointmentDao = new AppointmentDao_Impl(this);
        }
        return _appointmentDao;
      }
    }
  }

  @Override
  public ClinicalRecordDao clinicalRecordDao() {
    if (_clinicalRecordDao != null) {
      return _clinicalRecordDao;
    } else {
      synchronized(this) {
        if(_clinicalRecordDao == null) {
          _clinicalRecordDao = new ClinicalRecordDao_Impl(this);
        }
        return _clinicalRecordDao;
      }
    }
  }

  @Override
  public VitalsDao vitalsDao() {
    if (_vitalsDao != null) {
      return _vitalsDao;
    } else {
      synchronized(this) {
        if(_vitalsDao == null) {
          _vitalsDao = new VitalsDao_Impl(this);
        }
        return _vitalsDao;
      }
    }
  }
}
