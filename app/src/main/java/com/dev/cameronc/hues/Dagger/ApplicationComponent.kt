package com.dev.cameronc.hues.Dagger

import com.dev.cameronc.hues.Base.BaseActivity
import com.dev.cameronc.hues.Dagger.Connect.ConnectComponent
import com.dev.cameronc.hues.Dagger.Connect.ConnectModule
import com.dev.cameronc.hues.Dagger.Home.HomeComponent
import com.dev.cameronc.hues.Dagger.Home.HomeModule
import com.dev.cameronc.hues.Dagger.LightGroup.LightGroupComponent
import com.dev.cameronc.hues.Dagger.LightGroup.LightGroupModule
import com.dev.cameronc.hues.Preferences.SettingsActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by ccord on 11/9/2016.
 */

@Singleton
@Component(modules = arrayOf(AndroidModule::class, HueModule::class))
interface ApplicationComponent
{
    fun plus(connectModule: ConnectModule): ConnectComponent
    fun plus(homeModule: HomeModule): HomeComponent
    fun plus(lightGroupModule: LightGroupModule): LightGroupComponent
    fun inject(settings: SettingsActivity)
    fun inject(base: BaseActivity)
}
