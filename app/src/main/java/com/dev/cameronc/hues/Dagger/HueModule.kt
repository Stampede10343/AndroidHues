package com.dev.cameronc.hues.Dagger

import android.os.Build
import com.philips.lighting.hue.sdk.PHHueSDK
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by ccord on 11/9/2016.
 */

@Module
class HueModule
{
    @Provides
    @Singleton
    fun provideHueSdk(): PHHueSDK
    {
        val hueSdk = PHHueSDK.create()
        hueSdk.deviceName = Build.MODEL
        hueSdk.appName = "Hues"

        return hueSdk
    }
}
