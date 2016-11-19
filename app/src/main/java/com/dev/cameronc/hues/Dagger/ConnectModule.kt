package com.dev.cameronc.hues.Dagger

import com.dev.cameronc.hues.Connect.ConnectPresenter
import com.philips.lighting.hue.sdk.PHHueSDK
import dagger.Module
import dagger.Provides

/**
 * Created by ccord on 11/19/2016.
 */
@Module
class ConnectModule()
{
    @Provides
    fun providePresenter(phHueSDK: PHHueSDK): ConnectPresenter = ConnectPresenter(phHueSDK)
}