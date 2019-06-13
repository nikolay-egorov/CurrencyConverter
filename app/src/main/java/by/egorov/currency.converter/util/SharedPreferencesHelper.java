package by.egorov.currency.converter.util;

import android.content.SharedPreferences;

import by.egorov.currency.converter.controller.ConverterActivity;
import by.egorov.currency.converter.model.Cache;


public class SharedPreferencesHelper {

    private static final String PREFS_CACHE = "prefs_cache";


    public static SharedPreferences getSharedPreferences() {
        return ConverterActivity.getSharedPreferences();
    }


    public static boolean isCacheAvailable() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String json = sharedPreferences.getString(PREFS_CACHE, "");
        if (json.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static Cache getCache() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String json = sharedPreferences.getString(PREFS_CACHE, "");
        if (json.equals("")) {
            return null;
        } else {
            return Cache.deserialize(json);
        }
    }

    public static void saveCache(Cache cache) {
        saveString(PREFS_CACHE, Cache.serialize(cache));
    }


    private static void saveString(String prefKey, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString(prefKey, value);
        // consider switching to apply()
        prefEditor.apply();
    }

    private static void saveBoolean(String prefKey, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putBoolean(prefKey, value);
        // consider switching to apply()
        prefEditor.apply();
    }
}