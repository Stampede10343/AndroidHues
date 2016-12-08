package com.dev.cameronc.hues

import com.philips.lighting.hue.sdk.utilities.PHUtilities
import com.philips.lighting.model.PHBridge
import com.philips.lighting.model.PHGroup
import com.philips.lighting.model.PHLight
import com.philips.lighting.model.PHLightState

/**
 * Created by ccord on 12/5/2016.
 */
fun PHGroup.getGroupLights(bridge: PHBridge): List<PHLight>
{
    val lights = this.lightIdentifiers.flatMap { id -> bridge.resourceCache.allLights }.filter { light -> lightIdentifiers.contains(light.identifier) }
    return lights
}

fun PHLight.setColor(bridge: PHBridge, color: Int)
{
    val xy = PHUtilities.calculateXY(color, "model")
    val newState = PHLightState()
    newState.x = xy[0]
    newState.y = xy[1]
    bridge.updateLightState(this, newState)
}

fun PHLight.getColor() : Int
{
    val array = HueUtils.createColorArray(lastKnownLightState)
    val color = PHUtilities.colorFromXY(array, modelNumber)
    return color
}