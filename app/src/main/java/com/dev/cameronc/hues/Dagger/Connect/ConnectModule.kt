package com.dev.cameronc.hues.Dagger.Connect

import com.dev.cameronc.hues.Connect.ConnectContract
import com.dev.cameronc.hues.Connect.ConnectPresenter
import com.dev.cameronc.hues.SharedPrefs
import com.philips.lighting.hue.sdk.PHHueSDK
import dagger.Module
import dagger.Provides

/**
 * Created by ccord on 11/19/2016.
 */
@Module
class ConnectModule()
{
    @ConnectScope
    @Provides
    fun providePresenter(phHueSDK: PHHueSDK, sharedPrefs: SharedPrefs): ConnectContract.Presenter = ConnectPresenter(phHueSDK, sharedPrefs)
}