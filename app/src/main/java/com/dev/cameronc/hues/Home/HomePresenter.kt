package com.dev.cameronc.hues.Home

import com.dev.cameronc.hues.ColorPicker.ColorPickerListener
import com.dev.cameronc.hues.Preferences.PreferenceKeys
import com.dev.cameronc.hues.Preferences.SharedPrefs
import com.philips.lighting.hue.sdk.PHAccessPoint
import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.hue.sdk.PHSDKListener
import com.philips.lighting.model.PHBridge
import com.philips.lighting.model.PHHueParsingError
import com.philips.lighting.model.PHLight
import com.philips.lighting.model.PHLightState
import io.reactivex.Observable

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
    var lightObservable: Observable<Int>? = null

    override fun onViewAttached(view: HomeContract.View)
    {
        this.view = view

        if (connected)
        {
            view.showLightGroups(hueSDK.selectedBridge.resourceCache.allGroups)
        }
        else
        {
            tryConnectToLastAP(view)
        }

        lightObservable = getSliderChangedObservable(object : ColorPickerListener
        {
            override fun onSliderPercentChanged(percent: Int)
            {
            }
        })
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
                view.showLightGroups(hueSDK.selectedBridge.resourceCache.allGroups)
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

    private fun getSliderChangedObservable(listener: ColorPickerListener): Observable<Int>
    {
        return Observable.create<Int> { emitter ->
            val cpl = object : ColorPickerListener
            {
                override fun onSliderPercentChanged(percent: Int)
                {
                    if (!emitter.isDisposed)
                    {
                        emitter.onNext(percent)
                    }
                }
            }
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

        /*val light = phBridge.resourceCache.allLights[1]
        val lightState = PHLightState()
        val xy = PHUtilities.calculateXY(R.color.colorPrimary, light.modelNumber)

        lightState.isOn = true
        lightState.x = xy[0]
        lightState.y = xy[1]

        phBridge.updateLightState(light, lightState)*/

        view?.showLightGroups(phBridge.resourceCache.allGroups)
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

    override fun onSliderChanged(group: GroupUpdateEvent)
    {
        val ratio = group.percent / 100f
        val lightsMap = hueSDK.selectedBridge.resourceCache.lights
        val groupLights: List<PHLight>? = group.group.lightIdentifiers.map { id -> lightsMap[id] }.filterNotNull()

        groupLights?.forEachIndexed { i, phLight ->
            val lightState = PHLightState()
            val newBrightness = (254 * ratio).toInt()
            lightState.brightness = newBrightness

            hueSDK.selectedBridge.updateLightState(phLight, lightState)
        }

    }
}
