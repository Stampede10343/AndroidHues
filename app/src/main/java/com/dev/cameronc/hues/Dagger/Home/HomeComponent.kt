package com.dev.cameronc.hues.Dagger.Home

import com.dev.cameronc.hues.Home.HomeActivity
import dagger.Subcomponent

/**
 * Created by ccord on 11/19/2016.
 */
@HomeScope
@Subcomponent(modules = arrayOf(HomeModule::class))
interface HomeComponent
{
    fun inject(homeActivity: HomeActivity)
}