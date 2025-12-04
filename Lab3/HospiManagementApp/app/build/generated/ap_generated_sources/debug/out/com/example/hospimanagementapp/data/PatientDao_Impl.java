package com.example.hospimanagementapp.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
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
public final class PatientDao_Impl implements PatientDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Patient> __insertionAdapterOfPatient;

  public PatientDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPatient = new EntityInsertionAdapter<Patient>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `patients` (`id`,`nhsNumber`,`fullName`,`dateOfBirth`,`phone`,`email`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Patient entity) {
        statement.bindLong(1, entity.id);
        if (entity.nhsNumber == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.nhsNumber);
        }
        if (entity.fullName == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.fullName);
        }
        if (entity.dateOfBirth == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.dateOfBirth);
        }
        if (entity.phone == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.phone);
        }
        if (entity.email == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.email);
        }
        statement.bindLong(7, entity.createdAt);
        statement.bindLong(8, entity.updatedAt);
      }
    };
  }

  @Override
  public long insert(final Patient patient) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfPatient.insertAndReturnId(patient);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int countByNhs(final String nhsNumber) {
    final String _sql = "SELECT COUNT(*) FROM patients WHERE nhsNumber = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (nhsNumber == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nhsNumber);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
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
