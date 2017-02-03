package com.dev.cameronc.hues.Dagger

import android.content.Context
import com.dev.cameronc.hues.Preferences.AndroidSharedPrefs
import com.dev.cameronc.hues.Preferences.SharedPrefs
import com.dev.cameronc.hues.ThemeManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by ccord on 11/9/2016.
 */

@Module
class AndroidModule(context: Context)
{
    private val context: Context

    init
    {
        this.context = context.applicationContext
    }

    @Provides
    @Singleton
    fun context(): Context
    {
        return context
    }

    @Provides
    @Singleton
    fun sharedPreferences(context: Context): SharedPrefs
    {
        return AndroidSharedPrefs(context.getSharedPreferences("HuesApplication", Context.MODE_PRIVATE))
    }

    @Provides
    @Singleton
    fun themeManager(sharedPrefs: SharedPrefs): ThemeManager
    {
        return ThemeManager(sharedPrefs)
    }
}
