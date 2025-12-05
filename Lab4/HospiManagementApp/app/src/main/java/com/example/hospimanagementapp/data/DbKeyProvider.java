package com.example.hospimanagementapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.SecureRandom;

public final class DbKeyProvider {

    private static final String Pref_Name = "db_secure_pref";
    private static final String Key_DB = "encrypted_db_key";

    private DbKeyProvider() {
    }

    public static byte[] getOrCreateKey(Context ctx) {
        SharedPreferences sPreferences = ctx.getSharedPreferences(Pref_Name, Context.MODE_PRIVATE);
        String stored = sPreferences.getString(Key_DB, null);
        if (stored != null) {
            return Base64.decode(stored, Base64.NO_WRAP);
        }

        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);

        sPreferences.edit()
                .putString(Key_DB, Base64.encodeToString(key, Base64.NO_WRAP))
                .apply();

        return key;
    }

}
