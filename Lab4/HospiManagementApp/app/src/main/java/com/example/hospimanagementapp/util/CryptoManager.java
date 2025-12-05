package com.example.hospimanagementapp.util;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public final class CryptoManager {

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "hms_phi_key";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE_BYTES = 12;
    private static final int TAG_LENGTH_BITS = 128;

    private CryptoManager() {}

    private static SecretKey useSecretKey() throws Exception {
        KeyStore kStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        kStore.load(null);

        if (kStore.containsAlias(KEY_ALIAS)) {
            KeyStore.SecretKeyEntry secretKeyEntry =
                    (KeyStore.SecretKeyEntry) kStore.getEntry(KEY_ALIAS, null);
            return secretKeyEntry.getSecretKey();
        }

        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);

        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(KEY_SIZE)
                .build();

        keyGenerator.init(keyGenParameterSpec);
        return keyGenerator.generateKey();
    }


    public static String encrypt(String plainText) {
        if (plainText == null || plainText.trim().isEmpty()) {
            return plainText;
        }

        try {
            SecretKey key = useSecretKey();
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] iv = cipher.getIV();
            byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            ByteBuffer bBuffer = ByteBuffer.allocate(IV_SIZE_BYTES + cipherText.length);
            bBuffer.put(iv);
            bBuffer.put(cipherText);

            return Base64.encodeToString(bBuffer.array(), Base64.NO_WRAP);
        } catch (Exception e) {
            return plainText;
        }
    }


    public static String decrypt(String encryption) {
        if (encryption == null || encryption.trim().isEmpty()) {
            return encryption;
        }

        try {
            byte[] all = Base64.decode(encryption, Base64.NO_WRAP);
            if (all.length <= IV_SIZE_BYTES) {
                return encryption;
            }

            byte[] iv = new byte[IV_SIZE_BYTES];
            byte[] cipherText = new byte[all.length - IV_SIZE_BYTES];

            System.arraycopy(all, 0, iv, 0, IV_SIZE_BYTES);
            System.arraycopy(all, IV_SIZE_BYTES, cipherText, 0, cipherText.length);

            SecretKey key = useSecretKey();
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BITS, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            byte[] plainBytes = cipher.doFinal(cipherText);
            return new String(plainBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return encryption;
        }
    }


    public static String safeDisplay(String encryption) {
        String val = decrypt(encryption);
        if (val == null || val.trim().isEmpty()) {
            return "N/A";
        }
        return val;
    }
}
