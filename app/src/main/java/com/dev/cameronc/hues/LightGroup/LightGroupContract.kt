package com.dev.cameronc.hues.LightGroup

import com.philips.lighting.model.PHLight
import io.reactivex.Observable

/**
 * Created by ccord on 12/5/2016.
 */
interface LightGroupContract
{
    interface View
    {
        fun showLights(lights: List<PHLight>?)
        fun showLightColorPicker(lightItem: PHLight)
    }

    interface Presenter : com.dev.cameronc.hues.Base.Presenter<View>
    {
        fun onSliderChanged(event: LightGroupActivity.LightUpdateEvent)
        fun onSwitchToggled(light: PHLight, on: Boolean)
        fun onLightClicked(lightItem: PHLight)
        fun onLightColorChanged(color: Int)
        fun onLightColorSelected(color: Int)
        fun colorChangeObservable(colorChanged: Observable<Int>)
    }
}