package com.example.hospimanagementapp.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class VitalsDao_Impl implements VitalsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Vitals> __insertionAdapterOfVitals;

  private final SharedSQLiteStatement __preparedStmtOfMarkSynced;

  public VitalsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVitals = new EntityInsertionAdapter<Vitals>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `vitals` (`id`,`patientNHS`,`temperature`,`heartRate`,`glucose`,`oxygenLevel`,`timestamp`,`synced`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Vitals entity) {
        statement.bindLong(1, entity.id);
        if (entity.patientNHS == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.patientNHS);
        }
        statement.bindDouble(3, entity.temperature);
        statement.bindLong(4, entity.heartRate);
        statement.bindLong(5, entity.glucose);
        statement.bindLong(6, entity.oxygenLevel);
        statement.bindLong(7, entity.timestamp);
        final int _tmp = entity.synced ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
    this.__preparedStmtOfMarkSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE vitals SET synced = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final Vitals v) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfVitals.insertAndReturnId(v);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void markSynced(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSynced.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfMarkSynced.release(_stmt);
    }
  }

  @Override
  public List<Vitals> getPending() {
    final String _sql = "SELECT * FROM vitals WHERE synced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPatientNHS = CursorUtil.getColumnIndexOrThrow(_cursor, "patientNHS");
      final int _cursorIndexOfTemperature = CursorUtil.getColumnIndexOrThrow(_cursor, "temperature");
      final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
      final int _cursorIndexOfGlucose = CursorUtil.getColumnIndexOrThrow(_cursor, "glucose");
      final int _cursorIndexOfOxygenLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "oxygenLevel");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
      final List<Vitals> _result = new ArrayList<Vitals>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Vitals _item;
        _item = new Vitals();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfPatientNHS)) {
          _item.patientNHS = null;
        } else {
          _item.patientNHS = _cursor.getString(_cursorIndexOfPatientNHS);
        }
        _item.temperature = _cursor.getFloat(_cursorIndexOfTemperature);
        _item.heartRate = _cursor.getInt(_cursorIndexOfHeartRate);
        _item.glucose = _cursor.getInt(_cursorIndexOfGlucose);
        _item.oxygenLevel = _cursor.getInt(_cursorIndexOfOxygenLevel);
        _item.timestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfSynced);
        _item.synced = _tmp != 0;
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countForPatient(final String nhs) {
    final String _sql = "SELECT COUNT(*) FROM vitals WHERE patientNHS = ?";
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

  @Override
  public List<Vitals> pageForPatient(final String nhs, final int limit, final int offset) {
    final String _sql = "SELECT * FROM vitals WHERE patientNHS = ? ORDER BY timestamp DESC LIMIT ? OFFSET ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (nhs == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nhs);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 3;
    _statement.bindLong(_argIndex, offset);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPatientNHS = CursorUtil.getColumnIndexOrThrow(_cursor, "patientNHS");
      final int _cursorIndexOfTemperature = CursorUtil.getColumnIndexOrThrow(_cursor, "temperature");
      final int _cursorIndexOfHeartRate = CursorUtil.getColumnIndexOrThrow(_cursor, "heartRate");
      final int _cursorIndexOfGlucose = CursorUtil.getColumnIndexOrThrow(_cursor, "glucose");
      final int _cursorIndexOfOxygenLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "oxygenLevel");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "synced");
      final List<Vitals> _result = new ArrayList<Vitals>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Vitals _item;
        _item = new Vitals();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfPatientNHS)) {
          _item.patientNHS = null;
        } else {
          _item.patientNHS = _cursor.getString(_cursorIndexOfPatientNHS);
        }
        _item.temperature = _cursor.getFloat(_cursorIndexOfTemperature);
        _item.heartRate = _cursor.getInt(_cursorIndexOfHeartRate);
        _item.glucose = _cursor.getInt(_cursorIndexOfGlucose);
        _item.oxygenLevel = _cursor.getInt(_cursorIndexOfOxygenLevel);
        _item.timestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfSynced);
        _item.synced = _tmp != 0;
        _result.add(_item);
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
