package com.example.hospimanagementapp.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ClinicalRecordDao_Impl implements ClinicalRecordDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ClinicalRecord> __insertionAdapterOfClinicalRecord;

  public ClinicalRecordDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfClinicalRecord = new EntityInsertionAdapter<ClinicalRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `clinical_records` (`id`,`patientNHS`,`allergies`,`medications`,`conditions`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final ClinicalRecord entity) {
        statement.bindLong(1, entity.id);
        if (entity.patientNHS == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.patientNHS);
        }
        if (entity.allergies == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.allergies);
        }
        if (entity.medications == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.medications);
        }
        if (entity.conditions == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.conditions);
        }
        statement.bindLong(6, entity.updatedAt);
      }
    };
  }

  @Override
  public long upsert(final ClinicalRecord record) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfClinicalRecord.insertAndReturnId(record);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public ClinicalRecord findByPatient(final String nhs) {
    final String _sql = "SELECT * FROM clinical_records WHERE patientNHS = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (nhs == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nhs);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPatientNHS = CursorUtil.getColumnIndexOrThrow(_cursor, "patientNHS");
      final int _cursorIndexOfAllergies = CursorUtil.getColumnIndexOrThrow(_cursor, "allergies");
      final int _cursorIndexOfMedications = CursorUtil.getColumnIndexOrThrow(_cursor, "medications");
      final int _cursorIndexOfConditions = CursorUtil.getColumnIndexOrThrow(_cursor, "conditions");
      final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
      final ClinicalRecord _result;
      if (_cursor.moveToFirst()) {
        _result = new ClinicalRecord();
        _result.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfPatientNHS)) {
          _result.patientNHS = null;
        } else {
          _result.patientNHS = _cursor.getString(_cursorIndexOfPatientNHS);
        }
        if (_cursor.isNull(_cursorIndexOfAllergies)) {
          _result.allergies = null;
        } else {
          _result.allergies = _cursor.getString(_cursorIndexOfAllergies);
        }
        if (_cursor.isNull(_cursorIndexOfMedications)) {
          _result.medications = null;
        } else {
          _result.medications = _cursor.getString(_cursorIndexOfMedications);
        }
        if (_cursor.isNull(_cursorIndexOfConditions)) {
          _result.conditions = null;
        } else {
          _result.conditions = _cursor.getString(_cursorIndexOfConditions);
        }
        _result.updatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
