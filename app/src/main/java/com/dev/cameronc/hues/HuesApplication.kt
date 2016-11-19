package com.dev.cameronc.hues

import android.app.Application
import com.dev.cameronc.hues.Dagger.*

/**
 * Created by ccord on 11/9/2016.
 */

class HuesApplication : Application()
{
    private var applicationComponent: ApplicationComponent? = null
    private var connectComponent: ConnectComponent? = null

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

    fun getConnectComponent(): ConnectComponent
    {
        if(connectComponent == null)
        {
            connectComponent = getApplicationComponent().plus(ConnectModule())
        }

        return connectComponent!!
    }
}
