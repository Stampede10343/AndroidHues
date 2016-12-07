package com.dev.cameronc.hues.LightGroup

import com.dev.cameronc.hues.Home.GroupUpdateEvent
import com.dev.cameronc.hues.getGroupLights
import com.philips.lighting.hue.sdk.PHAccessPoint
import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.hue.sdk.PHSDKListener
import com.philips.lighting.model.*

/**
 * Created by ccord on 12/5/2016.
 */
class LightGroupPresenter(val hueSDK: PHHueSDK) : LightGroupContract.Presenter, PHSDKListener
{
    var view: LightGroupContract.View? = null
    var groupId: String? = null
        set(value)
        {
            field = value
            findGroup()
        }
    var group: PHGroup? = null
    var lights: List<PHLight>? = null

    init
    {
        hueSDK.notificationManager.registerSDKListener(this)
    }

    override fun onViewAttached(view: LightGroupContract.View)
    {
        this.view = view

        if (lights == null)
        {
            if (groupId != null && group == null)
            {
                findGroup()
            }
            lights = group?.getGroupLights(hueSDK.selectedBridge)
            view.showLights(lights)
        }
        else
        {
            view.showLights(lights)
        }
    }

    private fun findGroup()
    {
        group = hueSDK.selectedBridge.resourceCache.allGroups.findLast { group -> group.identifier == groupId }
    }

    override fun onViewDetached()
    {
        view = null
    }

    override fun onSliderChanged(event: LightGroupActivity.LightUpdateEvent)
    {
        val newState = PHLightState()
        newState.brightness = event.value
        hueSDK.selectedBridge.updateLightState(event.light, newState)
    }

    override fun onSwitchToggled(light: PHLight, on: Boolean)
    {
        val newState = PHLightState()
        newState.isOn = on
        hueSDK.selectedBridge.updateLightState(light, newState)
    }

    override fun onAccessPointsFound(p0: MutableList<PHAccessPoint>?)
    {
    }

    override fun onAuthenticationRequired(p0: PHAccessPoint?)
    {
    }

    override fun onBridgeConnected(p0: PHBridge?, p1: String?)
    {
    }

    override fun onCacheUpdated(p0: MutableList<Int>?, p1: PHBridge?)
    {
    }

    override fun onConnectionLost(p0: PHAccessPoint?)
    {
    }

    override fun onConnectionResumed(p0: PHBridge?)
    {
    }

    override fun onError(p0: Int, p1: String?)
    {
    }

    override fun onParsingErrors(p0: MutableList<PHHueParsingError>?)
    {
    }
}
