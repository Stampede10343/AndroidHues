package com.dev.cameronc.hues.Dagger;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ccord on 11/9/2016.
 */

@Module
public class AndroidModule
{
    private Context context;

    public AndroidModule(Context context)
    {
        this.context = context.getApplicationContext();
    }

    @Provides
    @Singleton
    public Context context()
    {
        return context;
    }

    @Provides
    @Singleton
    public SharedPreferences sharedPreferences(Context context)
    {
        return context.getSharedPreferences("HuesApplication", Context.MODE_PRIVATE);
    }
}
