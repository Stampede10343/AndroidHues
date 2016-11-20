package com.dev.cameronc.hues.Dagger

import android.content.Context
import com.dev.cameronc.hues.AndroidSharedPrefs
import com.dev.cameronc.hues.SharedPrefs
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
}
