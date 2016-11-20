package com.dev.cameronc.hues.Home

import com.dev.cameronc.hues.PreferenceKeys
import com.dev.cameronc.hues.SharedPrefs
import com.philips.lighting.hue.sdk.PHAccessPoint
import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.hue.sdk.PHSDKListener
import com.philips.lighting.model.PHBridge
import com.philips.lighting.model.PHHueParsingError

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
    override fun onViewAttached(view: HomeContract.View)
    {
        this.view = view

        val lastBridgeIP = sharedPrefs.getString(PreferenceKeys.LAST_BRIDGE_IP)
        val lastBridgeMacAddress = sharedPrefs.getString(PreferenceKeys.LAST_BRIDGE_MAC_ADDRESS)
        val lastBridgeUsername = sharedPrefs.getString(PreferenceKeys.LAST_BRIDGE_USERNAME)

        if (lastBridgeIP.isNotEmpty() && lastBridgeMacAddress.isNotEmpty())
        {
            val accessPoint = PHAccessPoint(lastBridgeIP, lastBridgeUsername, lastBridgeMacAddress)
            if (!hueSDK.isAccessPointConnected(accessPoint))
            {
                hueSDK.connect(accessPoint)
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

    override fun onBridgeConnected(phBridge: PHBridge?, username: String?)
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
