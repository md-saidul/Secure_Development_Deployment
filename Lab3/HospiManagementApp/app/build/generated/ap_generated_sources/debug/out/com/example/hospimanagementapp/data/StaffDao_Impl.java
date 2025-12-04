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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StaffDao_Impl implements StaffDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Staff> __insertionAdapterOfStaff;

  public StaffDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStaff = new EntityInsertionAdapter<Staff>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `staff` (`id`,`fullName`,`email`,`role`,`adminPin`,`clinic`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Staff entity) {
        statement.bindLong(1, entity.id);
        if (entity.fullName == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.fullName);
        }
        if (entity.email == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.email);
        }
        final String _tmp = Staff.fromRole(entity.role);
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        if (entity.adminPin == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.adminPin);
        }
        if (entity.clinic == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.clinic);
        }
      }
    };
  }

  @Override
  public long insert(final Staff staff) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfStaff.insertAndReturnId(staff);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Staff> getAll() {
    final String _sql = "SELECT * FROM staff ORDER BY fullName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
      final int _cursorIndexOfAdminPin = CursorUtil.getColumnIndexOrThrow(_cursor, "adminPin");
      final int _cursorIndexOfClinic = CursorUtil.getColumnIndexOrThrow(_cursor, "clinic");
      final List<Staff> _result = new ArrayList<Staff>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Staff _item;
        _item = new Staff();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _item.fullName = null;
        } else {
          _item.fullName = _cursor.getString(_cursorIndexOfFullName);
        }
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _item.email = null;
        } else {
          _item.email = _cursor.getString(_cursorIndexOfEmail);
        }
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfRole)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfRole);
        }
        _item.role = Staff.toRole(_tmp);
        if (_cursor.isNull(_cursorIndexOfAdminPin)) {
          _item.adminPin = null;
        } else {
          _item.adminPin = _cursor.getString(_cursorIndexOfAdminPin);
        }
        if (_cursor.isNull(_cursorIndexOfClinic)) {
          _item.clinic = null;
        } else {
          _item.clinic = _cursor.getString(_cursorIndexOfClinic);
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
  public int countAdmins() {
    final String _sql = "SELECT COUNT(*) FROM staff WHERE role = 'ADMIN'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
  public Staff findByEmail(final String email) {
    final String _sql = "SELECT * FROM staff WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
      final int _cursorIndexOfAdminPin = CursorUtil.getColumnIndexOrThrow(_cursor, "adminPin");
      final int _cursorIndexOfClinic = CursorUtil.getColumnIndexOrThrow(_cursor, "clinic");
      final Staff _result;
      if (_cursor.moveToFirst()) {
        _result = new Staff();
        _result.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _result.fullName = null;
        } else {
          _result.fullName = _cursor.getString(_cursorIndexOfFullName);
        }
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _result.email = null;
        } else {
          _result.email = _cursor.getString(_cursorIndexOfEmail);
        }
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfRole)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfRole);
        }
        _result.role = Staff.toRole(_tmp);
        if (_cursor.isNull(_cursorIndexOfAdminPin)) {
          _result.adminPin = null;
        } else {
          _result.adminPin = _cursor.getString(_cursorIndexOfAdminPin);
        }
        if (_cursor.isNull(_cursorIndexOfClinic)) {
          _result.clinic = null;
        } else {
          _result.clinic = _cursor.getString(_cursorIndexOfClinic);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Staff> getClinicians() {
    final String _sql = "SELECT * FROM staff WHERE role = 'CLINICIAN' ORDER BY fullName ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
      final int _cursorIndexOfAdminPin = CursorUtil.getColumnIndexOrThrow(_cursor, "adminPin");
      final int _cursorIndexOfClinic = CursorUtil.getColumnIndexOrThrow(_cursor, "clinic");
      final List<Staff> _result = new ArrayList<Staff>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Staff _item;
        _item = new Staff();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfFullName)) {
          _item.fullName = null;
        } else {
          _item.fullName = _cursor.getString(_cursorIndexOfFullName);
        }
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _item.email = null;
        } else {
          _item.email = _cursor.getString(_cursorIndexOfEmail);
        }
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfRole)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfRole);
        }
        _item.role = Staff.toRole(_tmp);
        if (_cursor.isNull(_cursorIndexOfAdminPin)) {
          _item.adminPin = null;
        } else {
          _item.adminPin = _cursor.getString(_cursorIndexOfAdminPin);
        }
        if (_cursor.isNull(_cursorIndexOfClinic)) {
          _item.clinic = null;
        } else {
          _item.clinic = _cursor.getString(_cursorIndexOfClinic);
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
