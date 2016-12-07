package com.dev.cameronc.hues

import android.app.Application
import com.dev.cameronc.hues.Dagger.AndroidModule
import com.dev.cameronc.hues.Dagger.ApplicationComponent
import com.dev.cameronc.hues.Dagger.Connect.ConnectComponent
import com.dev.cameronc.hues.Dagger.Connect.ConnectModule
import com.dev.cameronc.hues.Dagger.DaggerApplicationComponent
import com.dev.cameronc.hues.Dagger.Home.HomeComponent
import com.dev.cameronc.hues.Dagger.Home.HomeModule
import com.dev.cameronc.hues.Dagger.HueModule
import com.dev.cameronc.hues.Dagger.LightGroup.LightGroupComponent
import com.dev.cameronc.hues.Dagger.LightGroup.LightGroupModule

/**
 * Created by ccord on 11/9/2016.
 */

class HuesApplication : Application()
{
    private var applicationComponent: ApplicationComponent? = null
    private var connectComponent: ConnectComponent? = null
    private var homeComponent: HomeComponent? = null
    private var lightGroupComponent: LightGroupComponent? = null

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
        if (connectComponent == null)
        {
            connectComponent = getApplicationComponent().plus(ConnectModule())
        }

        return connectComponent!!
    }

    fun releaseConnectComponent()
    {
        connectComponent = null
    }

    fun getHomeComponent(): HomeComponent
    {
        if (homeComponent == null)
        {
            homeComponent = getApplicationComponent().plus(HomeModule())
        }

        return homeComponent!!
    }

    fun releaseHomeComponent()
    {
        homeComponent = null
    }

    fun  getLightGroupComponent(): LightGroupComponent
    {
        if(lightGroupComponent == null)
        {
            lightGroupComponent = getApplicationComponent().plus(LightGroupModule())
        }

        return lightGroupComponent!!
    }

    fun releaseLightGroupComponent()
    {
        lightGroupComponent = null
    }
}
