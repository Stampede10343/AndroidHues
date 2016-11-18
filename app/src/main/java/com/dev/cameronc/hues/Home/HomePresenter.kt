package com.dev.cameronc.hues.Home

import com.philips.lighting.hue.sdk.PHHueSDK

/**
 * Created by ccord on 11/9/2016.
 */

class HomePresenter(private val hueSDK: PHHueSDK) : HomeContract.Presenter<HomeContract.View>
{

    var view: HomeContract.View? = null

    override fun onViewAttached(view: HomeContract.View)
    {
        this.view = view

        val bridges = hueSDK.allBridges
        val builder = StringBuilder()
        for (bridge in bridges)
        {
            builder.append(bridge.toString())
        }

        view.showBridges(builder.toString())
    }

    override fun onViewDetached()
    {
        view = null
    }
}
