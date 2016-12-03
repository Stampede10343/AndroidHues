package com.dev.cameronc.hues.ColorPicker

import android.support.annotation.IntRange

/**
 * Created by ccord on 11/26/2016.
 */
interface ColorPickerListener
{
    fun onSliderPercentChanged(@IntRange(from = 0, to = 100) percent: Int)
}