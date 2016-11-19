package com.dev.cameronc.hues.Dagger

import com.dev.cameronc.hues.Connect.ConnectActivity
import dagger.Subcomponent

/**
 * Created by ccord on 11/19/2016.
 */
@Subcomponent(modules = arrayOf(ConnectModule::class))
interface ConnectComponent
{
    fun inject(connectActivity: ConnectActivity)
}