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

    String DISEASE_NAME = "";

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

    public void setUSERNAME(String USERNAME) {
        editor.putString("username", USERNAME).commit();
    }

    public void setPHONE(String PHONE) {
        editor.putString("phone", PHONE).commit();
    }

    public void setEMAIL(String EMAIL) {
        editor.putString("email", EMAIL).commit();
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

    public void logout() {
        editor.clear().commit();
    }

    public void setDiseaseName(Model model) {
        DISEASE_NAME = model.getDiseaseName();
        editor.putString("disease_name", DISEASE_NAME).commit();
    }

    public String getDiseaseName() {
        return sharedPreferences.getString("disease_name", "No data");
    }
}
