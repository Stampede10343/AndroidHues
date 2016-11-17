package com.dev.cameronc.hues.Base

/**
 * Created by ccord on 11/9/2016.
 */

interface Presenter<V>
{
    var view: V?
    fun onViewAttached(view: V)
    fun onViewDetached()
}
