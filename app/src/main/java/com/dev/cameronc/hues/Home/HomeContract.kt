package com.dev.cameronc.hues.Home

import com.philips.lighting.model.PHGroup

/**
 * Created by ccord on 11/9/2016.
 */

interface HomeContract
{
    interface View
    {
        fun navigateToConnectScreen()
        fun notifyBridgeConnected()
        fun showLightGroups(allGroups: List<PHGroup>)
        fun showNoLightGroups()
    }

    interface Presenter : com.dev.cameronc.hues.Base.Presenter<View>
    {
        fun onPresenterDestroyed()
        fun onSliderChanged(group: GroupUpdateEvent)
    }
}
