package com.dev.cameronc.hues.LightGroup

import com.philips.lighting.model.PHLight

/**
 * Created by ccord on 12/5/2016.
 */
interface LightGroupContract
{
    interface View
    {
        fun showLights(lights: List<PHLight>?)
    }

    interface Presenter : com.dev.cameronc.hues.Base.Presenter<View>
    {
        fun onSliderChanged(event: LightGroupActivity.LightUpdateEvent)
        fun onSwitchToggled(light: PHLight, on: Boolean)
    }
}