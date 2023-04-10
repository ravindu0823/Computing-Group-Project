package com.dsstudio.farmy;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session_user";
    String USERNAME = "";
    String PHONE = "";
    String EMAIL = "";
    String PASSWORD = "";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user) {
        USERNAME = user.getUsername();
        PHONE = user.getPhone();
        EMAIL = user.getEmail();
        PASSWORD = user.getPassword();

        editor.putString("username", USERNAME).commit();
        editor.putString("phone", PHONE).commit();
        editor.putString("email", EMAIL).commit();
        editor.putString("password", PASSWORD).commit();
    }

    public String getUsername() {
        return sharedPreferences.getString("username", null);
    }

    public String getPhone() {
        return sharedPreferences.getString("phone", null);
    }

    public String getEmail() {
        return sharedPreferences.getString("email", null);
    }

    public String getPassword() {
        return sharedPreferences.getString("password", null);
    }
}