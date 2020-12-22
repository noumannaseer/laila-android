package com.fantechlabs.lailaa.utils;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.NonNull;
import lombok.val;

import static android.content.Context.MODE_PRIVATE;
import static com.fantechlabs.lailaa.utils.AndroidUtil.getApplicationContext;

//*****************************************
public class SharedPreferencesUtils
//*****************************************
{


    public static final String PREF_KEY_FILE_NAME = getApplicationContext()
            .getPackageName();

    public static String USER_ID = "USER_ID";

    //*****************************************
    public static void setValue(String key, boolean status)
    //*****************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, status);
        editor.commit();
    }

    //*****************************************
    public static void setValue(String key, String value)
    //*****************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void setValue(String key, int value)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
        editor.commit();
    }


    public static void setListValue(String key, String item)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> stringSet = sharedPreferences.getStringSet(key, null);
        if (stringSet == null)
            stringSet = new HashSet<>();
        stringSet.add(item);
        editor.putStringSet(key, stringSet);
        editor.apply();
        editor.commit();
    }

    public static List<String> getListValue(String key)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> stringSet = sharedPreferences.getStringSet(key, null);
        if (stringSet == null)
            stringSet = new HashSet<>();
        List<String> stringList = new ArrayList<>();
        for (val setItem : stringSet)
        {
            stringList.add(setItem);
        }

        return stringList;
    }


    public static int getInt(String key)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        int value = sharedPreferences.getInt(key, -1);
        return value;

    }


    public static boolean getBoolean(String key)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(key, false);
        return value;

    }

    public static String getString(String key)
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                                      MODE_PRIVATE);
        String value = sharedPreferences.getString(key, null);
        return value;
    }

    //*************************************************************************
    public static void setValue(String key, Object object)
    //*************************************************************************
    {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                        MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.commit();
    }

    //*************************************************************************
    public static <S> S getModel(Class<S> serviceClass, String key)
    //*************************************************************************
    {
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences(PREF_KEY_FILE_NAME,
                        MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        S obj = gson.fromJson(json, serviceClass);
        return obj;
    }

    public static final String HEADER_STRING = "HEADER_STRING";

    //*****************************************
    public static @Nullable
    String getHeader()
    //*****************************************
    {
        val preferences = AndroidUtil.getApplicationContext()
                                     .getSharedPreferences(PREF_KEY_FILE_NAME, MODE_PRIVATE);
        return preferences.getString(HEADER_STRING, null);
    }

    //*****************************************
    public static void setHeader(@NonNull String header)
    //*****************************************
    {
        val preferences = AndroidUtil.getApplicationContext().getSharedPreferences(PREF_KEY_FILE_NAME, MODE_PRIVATE);
        val editor = preferences.edit();
        editor.putString(HEADER_STRING, header);
        editor.commit();
    }

}
