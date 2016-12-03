package com.dev.cameronc.hues.Preferences

/**
 * Created by ccord on 11/19/2016.
 */
interface SharedPrefs
{
    fun putBoolean(key: String, value: Boolean)
    fun putInt(key: String, value: Int)
    fun putString(key: String, value:String)

    fun getBoolean(key: String): Boolean
    fun getInt(key: String): Int
    fun getString(key: String): String
}