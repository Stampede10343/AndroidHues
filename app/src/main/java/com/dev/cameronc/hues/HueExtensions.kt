package com.dev.cameronc.hues

import com.philips.lighting.model.PHBridge
import com.philips.lighting.model.PHGroup
import com.philips.lighting.model.PHLight

/**
 * Created by ccord on 12/5/2016.
 */
fun PHGroup.getGroupLights(bridge: PHBridge): List<PHLight>
{
    val lights = this.lightIdentifiers.flatMap { id -> bridge.resourceCache.allLights }.filter { light -> lightIdentifiers.contains(light.identifier) }
    return lights
}