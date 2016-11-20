package com.dev.cameronc.hues.Connect

import com.dev.cameronc.hues.Model.AccessPoint
import com.philips.lighting.hue.sdk.PHAccessPoint

/**
 * Created by ccord on 11/16/2016.
 */
interface ConnectContract
{
    interface View
    {
        fun showFindAPDialog()
        fun dismissFindApDialog()
        fun displayAccessPoints(accessPoints: MutableList<PHAccessPoint>)
        fun dismissAccessPointSelectionDialog()
        fun showAuthenticationScreen()
        fun showNoAccessPointsFound()
        fun showHomeScreen()
    }

    interface Presenter : com.dev.cameronc.hues.Base.Presenter<View>
    {
        fun onApClicked(accessPoint: AccessPoint)
        fun onPresenterDestroyed()
    }
}