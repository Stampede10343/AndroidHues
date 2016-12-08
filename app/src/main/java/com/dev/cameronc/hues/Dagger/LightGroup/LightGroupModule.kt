package com.dev.cameronc.hues.Dagger.LightGroup

import com.dev.cameronc.hues.LightGroup.LightGroupPresenter
import com.philips.lighting.hue.sdk.PHHueSDK
import dagger.Module
import dagger.Provides

/**
 * Created by ccord on 12/5/2016.
 */
@Module
class LightGroupModule
{
    @Provides
    @LightGroupScope
    fun getPresenter(hueSDK: PHHueSDK): LightGroupPresenter
    {
        return LightGroupPresenter(hueSDK)
    }
}