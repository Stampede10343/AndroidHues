package com.dev.cameronc.hues

import android.support.annotation.StyleRes
import com.dev.cameronc.hues.Preferences.SharedPrefs

/**
 * Created by ccord on 2/2/2017.
 */
class ThemeManager(var sharedPrefs: SharedPrefs)
{
    @StyleRes
    fun getTheme(): Int
    {
        // Defaults to false for dark theme
        if(sharedPrefs.getBoolean(THEME_KEY))
        {
            return R.style.LightAppTheme
        }
        else
        {
            return R.style.AppTheme
        }
    }

    fun setTheme(darkTheme: Boolean)
    {
        sharedPrefs.putBoolean(THEME_KEY, !darkTheme)
    }

    companion object
    {
        val THEME_KEY = "theme"
    }
}