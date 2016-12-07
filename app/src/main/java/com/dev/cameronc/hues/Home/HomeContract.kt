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
        fun showLightGroups(allGroups: List<LightGroupAdapter.HueGroupInfo>)
        fun showNoLightGroups()
        fun navigateToGroupScreen(hueGroup: PHGroup)
    }

    interface Presenter : com.dev.cameronc.hues.Base.Presenter<View>
    {
        fun onPresenterDestroyed()
        fun onSliderChanged(event: GroupUpdateEvent)
        fun onGroupOnToggled(phGroup: PHGroup, on: Boolean)
        fun onGroupClicked(hueGroup: PHGroup)
    }
}
