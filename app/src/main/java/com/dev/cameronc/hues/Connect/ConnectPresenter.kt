package com.dev.cameronc.hues.Connect

import com.dev.cameronc.hues.Model.AccessPoint
import com.dev.cameronc.hues.PreferenceKeys
import com.dev.cameronc.hues.SharedPrefs
import com.philips.lighting.hue.sdk.PHAccessPoint
import com.philips.lighting.hue.sdk.PHBridgeSearchManager
import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.hue.sdk.PHSDKListener
import com.philips.lighting.model.PHBridge
import com.philips.lighting.model.PHHueParsingError
import java.util.*

/**
 * Created by ccord on 11/16/2016.
 */
class ConnectPresenter(val hueSdk: PHHueSDK, val sharedPrefs: SharedPrefs) : ConnectContract.Presenter, PHSDKListener
{
    var view: ConnectContract.View? = null
    var findingAps = false
    val accessPoints: MutableList<AccessPoint> = ArrayList()
    private var isAuthenticating = false

    override fun onViewAttached(view: ConnectContract.View)
    {
        this.view = view

        if (!findingAps && accessPoints.isEmpty())
        {
            view.showFindAPDialog()
            hueSdk.notificationManager?.registerSDKListener(this)
            val searchManager = hueSdk.getSDKService(PHHueSDK.SEARCH_BRIDGE) as PHBridgeSearchManager
            searchManager.search(true, true)
            findingAps = true
        }
        else if (isAuthenticating)
        {
            view.showAuthenticationScreen()
        }
    }

    override fun onViewDetached()
    {
        view = null
    }

    override fun onPresenterDestroyed()
    {
        hueSdk.notificationManager.unregisterSDKListener(this)
    }

    override fun onApClicked(accessPoint: AccessPoint)
    {
        val bridge = hueSdk.selectedBridge
        if (bridge != null)
        {
            hueSdk.disableHeartbeat(bridge)
            hueSdk.disconnect(bridge)
        }

        hueSdk.connect(PHAccessPoint(accessPoint.ipAddress, accessPoint.username, accessPoint.macAddress))
    }

    override fun onAccessPointsFound(accessPoints: MutableList<PHAccessPoint>?)
    {
        view?.dismissFindApDialog()
        if (accessPoints != null && accessPoints.isNotEmpty())
        {
            this.accessPoints.clear()
            this.accessPoints.addAll(accessPoints.map(::AccessPoint))

            hueSdk.accessPointsFound.clear()
            hueSdk.accessPointsFound.addAll(accessPoints.filterNotNull())

            view?.displayAccessPoints(accessPoints)
        }
        else
        {
            view?.showNoAccessPointsFound()
        }
        findingAps = false
    }

    override fun onAuthenticationRequired(accessPoint: PHAccessPoint)
    {
        view?.dismissAccessPointSelectionDialog()

        hueSdk.startPushlinkAuthentication(accessPoint)
        view?.showAuthenticationScreen()

        isAuthenticating = true
    }

    override fun onBridgeConnected(phBridge: PHBridge?, username: String?)
    {
        isAuthenticating = false

        hueSdk.selectedBridge = phBridge
        hueSdk.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL.toLong())

        val ipAddress = phBridge?.resourceCache?.bridgeConfiguration?.ipAddress
        val macAddress = phBridge?.resourceCache?.bridgeConfiguration?.macAddress

        sharedPrefs.putString(PreferenceKeys.LAST_BRIDGE_IP, ipAddress ?: "")
        sharedPrefs.putString(PreferenceKeys.LAST_BRIDGE_MAC_ADDRESS, macAddress ?: "")
        sharedPrefs.putString(PreferenceKeys.LAST_BRIDGE_USERNAME, username ?: "")

        view?.showHomeScreen()
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

    override fun onError(errorCode: Int, errorMessage: String?)
    {
    }

    override fun onParsingErrors(p0: MutableList<PHHueParsingError>?)
    {
    }
}