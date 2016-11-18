package com.dev.cameronc.hues

import android.app.Application
import com.dev.cameronc.hues.Dagger.AndroidModule
import com.dev.cameronc.hues.Dagger.ApplicationComponent
import com.dev.cameronc.hues.Dagger.DaggerApplicationComponent
import com.dev.cameronc.hues.Dagger.HueModule

/**
 * Created by ccord on 11/9/2016.
 */

class HuesApplication : Application()
{
    private var applicationComponent: ApplicationComponent? = null

    override fun onCreate()
    {
        super.onCreate()

    }

    fun getApplicationComponent(): ApplicationComponent
    {
        if (applicationComponent == null)
        {
            applicationComponent = DaggerApplicationComponent.builder().hueModule(HueModule()).androidModule(AndroidModule(this)).build()
        }

        return applicationComponent!!
    }
}
