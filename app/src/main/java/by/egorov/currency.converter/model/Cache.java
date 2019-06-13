package by.egorov.currency.converter.model;

import android.support.v4.util.CircularArray;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import by.egorov.currency.converter.util.SharedPreferencesHelper;


public class Cache {

    private static Cache CACHE;

    public static Cache get() {
        if (!SharedPreferencesHelper.isCacheAvailable()) {
            CACHE = new Cache();
            SharedPreferencesHelper.saveCache(CACHE);
        } else {
            CACHE = SharedPreferencesHelper.getCache();
        }

        return CACHE;
    }

    CircularArray<HistoryItem> cachedHistory;

    public Cache() {
        cachedHistory = new CircularArray<HistoryItem>();
    }

    public CircularArray<HistoryItem> getCachedHistory() {
        return cachedHistory;
    }

    public static String serialize(Cache cache) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.enableComplexMapKeySerialization().setPrettyPrinting().create();
        return gson.toJson(cache);
    }

    public static Cache deserialize(String json) {
        Type type = new TypeToken<Cache>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public void update() {
        SharedPreferencesHelper.saveCache(this);
    }
}