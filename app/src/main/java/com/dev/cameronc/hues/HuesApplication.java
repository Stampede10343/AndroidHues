package com.dev.cameronc.hues;

import android.app.Application;
import android.support.annotation.NonNull;

import com.dev.cameronc.hues.Dagger.AndroidModule;
import com.dev.cameronc.hues.Dagger.ApplicationComponent;
import com.dev.cameronc.hues.Dagger.DaggerApplicationComponent;
import com.dev.cameronc.hues.Dagger.HueModule;

/**
 * Created by ccord on 11/9/2016.
 */

public class HuesApplication extends Application
{
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();

    }

    @NonNull
    public ApplicationComponent getApplicationComponent()
    {
        if (applicationComponent == null)
        {
            applicationComponent = DaggerApplicationComponent.builder().hueModule(new HueModule()).androidModule(new AndroidModule(this)).build();
        }

        return applicationComponent;
    }
}
