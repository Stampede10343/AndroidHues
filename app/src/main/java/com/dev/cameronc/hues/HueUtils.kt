package com.dev.cameronc.hues

import com.philips.lighting.model.PHLightState

/**
 * Created by ccord on 12/6/2016.
 */
class HueUtils
{
    companion object
    {
        fun createColorArray(lightState: PHLightState): FloatArray
        {
            val array = FloatArray(2)
            array[0] = lightState.x
            array[1] = lightState.y
            return array
        }
    }
}