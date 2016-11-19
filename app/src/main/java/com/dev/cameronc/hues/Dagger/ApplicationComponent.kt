package com.dev.cameronc.hues.Dagger

import dagger.Component
import javax.inject.Singleton

/**
 * Created by ccord on 11/9/2016.
 */

@Singleton
@Component(modules = arrayOf(AndroidModule::class, HueModule::class))
interface ApplicationComponent
{
    //fun inject(activity: BaseActivity)
    fun plus(connectModule: ConnectModule): ConnectComponent
}
