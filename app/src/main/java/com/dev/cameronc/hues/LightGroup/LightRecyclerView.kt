package com.dev.cameronc.hues.LightGroup

import android.content.Context
import android.content.res.ColorStateList
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.dev.cameronc.hues.Home.GroupAdapter
import com.philips.lighting.model.PHLight
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ccord on 12/7/2016.
 */
class LightRecyclerView : RecyclerView
{
    val subscription = CompositeDisposable()

    constructor(context: Context) : super(context)
    {
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    {
    }

    fun setLightColorObservable(colorChangedObservable: Observable<Int>, currentLight: PHLight?)
    {
        if (currentLight != null)
        {
            val index = adapter?.lights?.indexOf(currentLight)
            val view = layoutManager?.findViewByPosition(index ?: -1)
            if (view != null)
            {
                val lightGroupVH = getChildViewHolder(view) as GroupAdapter.LightGroupVH
                subscription.add(colorChangedObservable.subscribe { color -> lightGroupVH.groupIcon.imageTintList = ColorStateList.valueOf(color) })
            }
        }
    }

    override fun onDetachedFromWindow()
    {
        super.onDetachedFromWindow()
        subscription.clear()
    }

    override fun getAdapter(): LightAdapter?
    {
        return super.getAdapter() as? LightAdapter
    }
}