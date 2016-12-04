package com.dev.cameronc.hues.Home

import com.dev.cameronc.hues.Preferences.PreferenceKeys
import com.dev.cameronc.hues.Preferences.SharedPrefs
import com.philips.lighting.hue.sdk.PHAccessPoint
import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.hue.sdk.PHSDKListener
import com.philips.lighting.model.*

/**
 * Created by ccord on 11/9/2016.
 */

class HomePresenter(private val hueSDK: PHHueSDK, private val sharedPrefs: SharedPrefs) : HomeContract.Presenter, PHSDKListener
{
    init
    {
        hueSDK.notificationManager.registerSDKListener(this)
    }

    var view: HomeContract.View? = null
    var connected = false

    override fun onViewAttached(view: HomeContract.View)
    {
        this.view = view

        if (connected)
        {
            view.showLightGroups(getAllGroupsWithLightStatus(hueSDK.selectedBridge))
        }
        else
        {
            tryConnectToLastAP(view)
        }
    }

    private fun tryConnectToLastAP(view: HomeContract.View)
    {
        val lastBridgeIP = sharedPrefs.getString(PreferenceKeys.LAST_BRIDGE_IP)
        val lastBridgeMacAddress = sharedPrefs.getString(PreferenceKeys.LAST_BRIDGE_MAC_ADDRESS)
        val lastBridgeUsername = sharedPrefs.getString(PreferenceKeys.LAST_BRIDGE_USERNAME)

        val accessPoint = PHAccessPoint(lastBridgeIP, lastBridgeUsername, lastBridgeMacAddress)

        if (lastBridgeIP.isNotEmpty() && lastBridgeMacAddress.isNotEmpty() && !hueSDK.isAccessPointConnected(accessPoint))
        {
            hueSDK.connect(accessPoint)
        }
        else if (hueSDK.isAccessPointConnected(accessPoint))
        {
            connected = true
            if (hueSDK.selectedBridge.resourceCache.allGroups.size > 0)
            {
                val groupLights: List<HueGroupInfo> = getAllGroupsWithLightStatus(hueSDK.selectedBridge)
                view.showLightGroups(groupLights)
            }
            else
            {
                view.showNoLightGroups()
            }
        }
        else
        {
            hueSDK.notificationManager.unregisterSDKListener(this)
            view.navigateToConnectScreen()
        }
    }

    override fun onViewDetached()
    {
        view = null
    }

    override fun onPresenterDestroyed()
    {
        hueSDK.notificationManager.unregisterSDKListener(this)
    }

    override fun onAccessPointsFound(p0: MutableList<PHAccessPoint>?)
    {
    }

    override fun onAuthenticationRequired(p0: PHAccessPoint?)
    {
    }

    override fun onBridgeConnected(phBridge: PHBridge, username: String?)
    {
        hueSDK.selectedBridge = phBridge
        connected = true
        view?.notifyBridgeConnected()

        val groupLights: List<HueGroupInfo> = getAllGroupsWithLightStatus(phBridge)
        view?.showLightGroups(groupLights)
    }

    private fun getAllGroupsWithLightStatus(phBridge: PHBridge): List<HueGroupInfo>
    {
        val lightsMap = hueSDK.selectedBridge.resourceCache.lights

        return phBridge.resourceCache.allGroups
                .map { group ->
                    HueGroupInfo(group, group.lightIdentifiers
                            .map { id ->
                                lightsMap[id]
                            }
                            .first()!!.lastKnownLightState)
                }
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

    override fun onSliderChanged(event: GroupUpdateEvent)
    {
        val groupLights: List<PHLight> = getLightsForGroup(event.group)

        val ratio = event.percent / 100f
        groupLights.forEachIndexed { i, phLight ->
            val lightState = PHLightState()
            val newBrightness = (254 * ratio).toInt()
            lightState.brightness = newBrightness

            hueSDK.selectedBridge.updateLightState(phLight, lightState)
        }

    }

    private fun getLightsForGroup(group: PHGroup): List<PHLight>
    {
        val lightsMap = hueSDK.selectedBridge.resourceCache.lights
        val groupLights: List<PHLight> = group.lightIdentifiers.map { id -> lightsMap[id] }.filterNotNull()
        return groupLights
    }

    override fun onGroupOnToggled(phGroup: PHGroup, on: Boolean)
    {
        // Need to check if turning the lights on to try and restore them to their previous brightness
        val groupLights = getLightsForGroup(phGroup)
        groupLights.forEach { light ->
            val newState = PHLightState()
            newState.isOn = on
            hueSDK.selectedBridge.updateLightState(light, newState)
        }
    }
}
