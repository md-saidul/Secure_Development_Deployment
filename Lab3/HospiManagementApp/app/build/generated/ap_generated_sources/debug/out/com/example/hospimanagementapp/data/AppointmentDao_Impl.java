package com.example.hospimanagementapp.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppointmentDao_Impl implements AppointmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Appointment> __insertionAdapterOfAppointment;

  private final EntityDeletionOrUpdateAdapter<Appointment> __updateAdapterOfAppointment;

  public AppointmentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppointment = new EntityInsertionAdapter<Appointment>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `appointments` (`id`,`patientNhsNumber`,`startTime`,`endTime`,`clinicianId`,`clinicianName`,`clinic`,`status`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Appointment entity) {
        statement.bindLong(1, entity.id);
        if (entity.patientNhsNumber == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.patientNhsNumber);
        }
        statement.bindLong(3, entity.startTime);
        statement.bindLong(4, entity.endTime);
        statement.bindLong(5, entity.clinicianId);
        if (entity.clinicianName == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.clinicianName);
        }
        if (entity.clinic == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.clinic);
        }
        if (entity.status == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.status);
        }
      }
    };
    this.__updateAdapterOfAppointment = new EntityDeletionOrUpdateAdapter<Appointment>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `appointments` SET `id` = ?,`patientNhsNumber` = ?,`startTime` = ?,`endTime` = ?,`clinicianId` = ?,`clinicianName` = ?,`clinic` = ?,`status` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Appointment entity) {
        statement.bindLong(1, entity.id);
        if (entity.patientNhsNumber == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.patientNhsNumber);
        }
        statement.bindLong(3, entity.startTime);
        statement.bindLong(4, entity.endTime);
        statement.bindLong(5, entity.clinicianId);
        if (entity.clinicianName == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.clinicianName);
        }
        if (entity.clinic == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.clinic);
        }
        if (entity.status == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.status);
        }
        statement.bindLong(9, entity.id);
      }
    };
  }

  @Override
  public long insert(final Appointment appt) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfAppointment.insertAndReturnId(appt);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final Appointment appt) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total += __updateAdapterOfAppointment.handle(appt);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Appointment> findBetween(final long start, final long end, final String clinic) {
    final String _sql = "SELECT * FROM appointments WHERE (? = '' OR clinic = ?) AND startTime BETWEEN ? AND ? ORDER BY startTime ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    if (clinic == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, clinic);
    }
    _argIndex = 2;
    if (clinic == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, clinic);
    }
    _argIndex = 3;
    _statement.bindLong(_argIndex, start);
    _argIndex = 4;
    _statement.bindLong(_argIndex, end);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPatientNhsNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "patientNhsNumber");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfClinicianId = CursorUtil.getColumnIndexOrThrow(_cursor, "clinicianId");
      final int _cursorIndexOfClinicianName = CursorUtil.getColumnIndexOrThrow(_cursor, "clinicianName");
      final int _cursorIndexOfClinic = CursorUtil.getColumnIndexOrThrow(_cursor, "clinic");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final List<Appointment> _result = new ArrayList<Appointment>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Appointment _item;
        _item = new Appointment();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfPatientNhsNumber)) {
          _item.patientNhsNumber = null;
        } else {
          _item.patientNhsNumber = _cursor.getString(_cursorIndexOfPatientNhsNumber);
        }
        _item.startTime = _cursor.getLong(_cursorIndexOfStartTime);
        _item.endTime = _cursor.getLong(_cursorIndexOfEndTime);
        _item.clinicianId = _cursor.getLong(_cursorIndexOfClinicianId);
        if (_cursor.isNull(_cursorIndexOfClinicianName)) {
          _item.clinicianName = null;
        } else {
          _item.clinicianName = _cursor.getString(_cursorIndexOfClinicianName);
        }
        if (_cursor.isNull(_cursorIndexOfClinic)) {
          _item.clinic = null;
        } else {
          _item.clinic = _cursor.getString(_cursorIndexOfClinic);
        }
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _item.status = null;
        } else {
          _item.status = _cursor.getString(_cursorIndexOfStatus);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Appointment> overlapping(final long clinicianId, final long newStart,
      final long newEnd) {
    final String _sql = "SELECT * FROM appointments WHERE clinicianId = ? AND ( (startTime < ? AND endTime > ?) )";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, clinicianId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, newEnd);
    _argIndex = 3;
    _statement.bindLong(_argIndex, newStart);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfPatientNhsNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "patientNhsNumber");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfClinicianId = CursorUtil.getColumnIndexOrThrow(_cursor, "clinicianId");
      final int _cursorIndexOfClinicianName = CursorUtil.getColumnIndexOrThrow(_cursor, "clinicianName");
      final int _cursorIndexOfClinic = CursorUtil.getColumnIndexOrThrow(_cursor, "clinic");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final List<Appointment> _result = new ArrayList<Appointment>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Appointment _item;
        _item = new Appointment();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfPatientNhsNumber)) {
          _item.patientNhsNumber = null;
        } else {
          _item.patientNhsNumber = _cursor.getString(_cursorIndexOfPatientNhsNumber);
        }
        _item.startTime = _cursor.getLong(_cursorIndexOfStartTime);
        _item.endTime = _cursor.getLong(_cursorIndexOfEndTime);
        _item.clinicianId = _cursor.getLong(_cursorIndexOfClinicianId);
        if (_cursor.isNull(_cursorIndexOfClinicianName)) {
          _item.clinicianName = null;
        } else {
          _item.clinicianName = _cursor.getString(_cursorIndexOfClinicianName);
        }
        if (_cursor.isNull(_cursorIndexOfClinic)) {
          _item.clinic = null;
        } else {
          _item.clinic = _cursor.getString(_cursorIndexOfClinic);
        }
        if (_cursor.isNull(_cursorIndexOfStatus)) {
          _item.status = null;
        } else {
          _item.status = _cursor.getString(_cursorIndexOfStatus);
        }
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
