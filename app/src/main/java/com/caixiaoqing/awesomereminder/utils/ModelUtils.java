package com.caixiaoqing.awesomereminder.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Created by caixiaoqing on 3/12/16.
 */

public class ModelUtils {
    private static Gson gson = new Gson();
    private static String PREF_NAME = "models";

    public static void save(Context context, String key, Object object) {
        String jsonString = gson.toJson(object);
        context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().putString(key, jsonString).apply();
    }

    public static <T> T get(Context context, String key, TypeToken<T> typeToken){
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        try {
            return gson.fromJson(sp.getString(key, ""), typeToken.getType());
        }
        catch (JsonSyntaxException e){
            e.printStackTrace();
            return null;
        }
    }

}
