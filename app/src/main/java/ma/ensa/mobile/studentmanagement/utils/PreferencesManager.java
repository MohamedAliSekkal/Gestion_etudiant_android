package ma.ensa.mobile.studentmanagement.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String PREF_NAME = "ESMSPreferences";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE_CODE = "role_code";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private SharedPreferences prefs;

    public PreferencesManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Sauvegarder la session utilisateur
    public void saveUserSession(int userId, String username, String roleCode) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_ROLE_CODE, roleCode);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Vérifier si l'utilisateur est connecté
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Récupérer l'ID utilisateur
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    // Récupérer le nom d'utilisateur
    public String getUsername() {
        return prefs.getString(KEY_USERNAME, "");
    }

    // Récupérer le code rôle
    public String getRoleCode() {
        return prefs.getString(KEY_ROLE_CODE, "");
    }

    // Déconnexion
    public void clearSession() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}