package com.dev.cameronc.hues.Home

/**
 * Created by ccord on 11/9/2016.
 */

interface HomeContract
{
    interface View
    {
        fun showBridges(bridges: String)
    }

    interface Presenter<V> : com.dev.cameronc.hues.Base.Presenter<View>
    {}
}
