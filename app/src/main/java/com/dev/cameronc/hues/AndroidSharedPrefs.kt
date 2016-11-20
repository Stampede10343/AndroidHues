package com.dev.cameronc.hues

import android.content.SharedPreferences

/**
 * Created by ccord on 11/19/2016.
 */
class AndroidSharedPrefs(private val sharedPreferences: SharedPreferences) : SharedPrefs
{
    override fun getBoolean(key: String): Boolean
    {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun getInt(key: String): Int
    {
        return sharedPreferences.getInt(key, -1)
    }

    override fun getString(key: String): String
    {
        return sharedPreferences.getString(key, "")
    }

    override fun putBoolean(key: String, value: Boolean)
    {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun putInt(key: String, value: Int)
    {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun putString(key: String, value: String)
    {
        sharedPreferences.edit().putString(key, value).apply()
    }
}