package com.dev.cameronc.hues.Dagger.Home

import com.dev.cameronc.hues.Home.HomePresenter
import com.dev.cameronc.hues.Preferences.SharedPrefs
import com.philips.lighting.hue.sdk.PHHueSDK
import dagger.Module
import dagger.Provides

/**
 * Created by ccord on 11/19/2016.
 */
@Module
class HomeModule
{
    @HomeScope
    @Provides
    fun provideHomePresenter(huesdk: PHHueSDK, preferences: SharedPrefs): HomePresenter = HomePresenter(huesdk, preferences)
}