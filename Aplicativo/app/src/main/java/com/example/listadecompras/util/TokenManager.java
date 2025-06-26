package com.example.listadecompras.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {
    private static final String PREF_NAME = "prefs";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REFRESH_TOKEN = "refresh_token";

    private SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveTokens(String accessToken, String refreshToken) {
        prefs.edit()
                .putString(ACCESS_TOKEN, accessToken)
                .putString(REFRESH_TOKEN, refreshToken)
                .apply();
        Log.d("TokenManager", "Tokens salvos: access=" + accessToken + " refresh=" + refreshToken);
    }

    public String getAccessToken() {
        String token = prefs.getString(ACCESS_TOKEN, null);
        Log.d("TokenManager", "getAccessToken: " + token);
        return token;
    }

    public String getRefreshToken() {
        String token = prefs.getString(REFRESH_TOKEN, null);
        Log.d("TokenManager", "getRefreshToken: " + token);
        return token;
    }

    public void clear() {
        prefs.edit().clear().apply();
        Log.d("TokenManager", "Tokens apagados");
    }
}
