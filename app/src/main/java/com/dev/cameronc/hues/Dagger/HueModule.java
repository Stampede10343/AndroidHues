package com.dev.cameronc.hues.Dagger;

import com.philips.lighting.hue.sdk.PHHueSDK;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ccord on 11/9/2016.
 */

@Module
public class HueModule
{
    @Provides
    @Singleton
    public PHHueSDK provideHueSdk()
    {
        return PHHueSDK.create();
    }
}
