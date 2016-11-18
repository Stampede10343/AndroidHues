package com.dev.cameronc.hues.Connect

import com.philips.lighting.hue.sdk.PHAccessPoint
import com.philips.lighting.hue.sdk.PHBridgeSearchManager
import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.hue.sdk.PHSDKListener
import com.philips.lighting.model.PHBridge
import com.philips.lighting.model.PHHueParsingError
import javax.inject.Inject

/**
 * Created by ccord on 11/16/2016.
 */
class ConnectPresenter : ConnectContract.Presenter, PHSDKListener
{
    @Inject
    lateinit var hueSDK: PHHueSDK
    var view: ConnectContract.View? = null

    override fun onViewAttached(view: ConnectContract.View)
    {
        this.view = view
        view.showFindAPDialog()

        hueSDK.notificationManager?.registerSDKListener(this)
        val searchManager = hueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE) as PHBridgeSearchManager
        searchManager.search(true, true)
    }

    override fun onAccessPointsFound(accessPoints: MutableList<PHAccessPoint>?)
    {
        view?.dismissAPDialog()

        if (accessPoints != null)
        {
            view?.displayAccessPoints(accessPoints)
        }
        else
        {
            view?.showNoAccessPointsFound()
        }
    }

    override fun onAuthenticationRequired(accessPoint: PHAccessPoint?)
    {
        view?.showAuthenticationDialog(accessPoint)
    }

    override fun onBridgeConnected(phBridge: PHBridge?, p1: String?)
    {
        hueSDK.selectedBridge = phBridge
        hueSDK.enableHeartbeat(phBridge, PHHueSDK.HB_INTERVAL.toLong())
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

    override fun onViewDetached()
    {
        view = null
    }

    override fun onAccessPointClicked(accessPoint: PHAccessPoint)
    {
        hueSDK.connect(accessPoint)
    }
}