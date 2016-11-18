package com.dev.cameronc.hues.Connect

import com.philips.lighting.hue.sdk.PHAccessPoint

/**
 * Created by ccord on 11/16/2016.
 */
interface ConnectContract
{
    interface View
    {
        fun showFindAPDialog()
        fun dismissAPDialog()
        fun displayAccessPoints(accessPoints: MutableList<PHAccessPoint>)
        fun showAuthenticationDialog(accessPoint: PHAccessPoint?)
        fun showNoAccessPointsFound()
    }

    interface Presenter : com.dev.cameronc.hues.Base.Presenter<View>
    {
        fun onAccessPointClicked(accessPoint: PHAccessPoint)
    }
}