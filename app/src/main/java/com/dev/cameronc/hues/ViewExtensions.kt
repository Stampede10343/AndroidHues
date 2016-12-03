package com.dev.cameronc.hues

import android.util.DisplayMetrics
import android.view.View


/**
 * Created by ccord on 11/21/2016.
 */

fun View.pxToDp(px: Int): Int
{
    val densityDpi = this.context.resources.displayMetrics.densityDpi
    return (px / (densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat())).toInt()
}

fun View.dpToPx(dp: Int): Int
{
    val metrics = context.resources.displayMetrics
    val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT.toFloat())
    return px.toInt()
}
