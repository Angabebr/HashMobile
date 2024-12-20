package com.example.hashprog;

import android.util.Base64;

public class Base64Utils {
    public static String encodeToBase64(String input) {
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decodeFromBase64(String base64Input) {
        byte[] decodedBytes = Base64.decode(base64Input, Base64.DEFAULT);
        return new String(decodedBytes);
    }
}