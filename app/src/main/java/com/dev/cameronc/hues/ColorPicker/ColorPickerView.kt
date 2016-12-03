package com.dev.cameronc.hues.ColorPicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.*
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.FloatRange
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.dev.cameronc.hues.dpToPx
import java.util.*

/**
 * Created by ccord on 11/21/2016.
 */
class ColorPickerView : View
{
    private val defaultHeight: Int
    private var sliderHeight: Float = 0f
    private var sliderBarWidth: Int = 0

    private val sliderBarPaint: Paint
    private var sliderBarCenterX: Int = 0

    private var xRatio: Float = 0f
    private var displayText: Boolean = false

    private val sliderChangeListeners: MutableList<(Int) -> Unit> = ArrayList()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, @StyleRes styleAttr: Int) : super(context, attrs, styleAttr)
    constructor(context: Context, attrs: AttributeSet, @StyleRes styleAttr: Int, styleRes: Int) : super(context, attrs, styleAttr, styleRes)

    init
    {
        defaultHeight = dpToPx(32)

        sliderBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            strokeWidth = sliderBarWidth / 2f
            color = argb(120, 255, 255, 255)
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val requestedWidth = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        val requestedHeight = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var width: Int = 0
        var height: Int = 0

        when (widthMode)
        {
            MeasureSpec.EXACTLY -> width = requestedWidth
            MeasureSpec.AT_MOST -> width = requestedWidth
            MeasureSpec.UNSPECIFIED -> width = requestedWidth
        }

        when (heightMode)
        {
            MeasureSpec.EXACTLY -> height = requestedHeight
            MeasureSpec.AT_MOST -> height = Math.min(defaultHeight, requestedHeight)
            MeasureSpec.UNSPECIFIED -> height = defaultHeight
        }

        setMeasuredDimension(width, height)
    }

    private var clipRect: RectF = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
    {
        super.onSizeChanged(w, h, oldw, oldh)
        sliderHeight = h.toFloat()

        sliderBarWidth = (w / 10f).toInt()
    }

    override fun onDraw(canvas: Canvas)
    {
        drawSliderBar(canvas)
    }

    private fun drawSliderBar(canvas: Canvas)
    {
        if (sliderBarOffStartScreen())
        {
            drawSliderBarRect(canvas, 0f, sliderBarWidth.toFloat())
            notifySliderChangeListeners(0)
        }
        else if (sliderBarOffEndScreen())
        {
            drawSliderBarRect(canvas, width - sliderBarWidth.toFloat(), width.toFloat())
            notifySliderChangeListeners(100)
        }
        else
        {
            drawSliderBarRect(canvas, sliderBarCenterX - sliderBarWidth / 2f, sliderBarCenterX + sliderBarWidth / 2f)
            notifySliderChangeListeners(((sliderBarCenterX / width.toFloat()) * 100).toInt())
        }
    }

    private fun sliderBarOffEndScreen()
            = sliderBarCenterX + sliderBarWidth / 2 > width

    private fun sliderBarOffStartScreen()
            = sliderBarCenterX - sliderBarWidth / 2 < 0

    private fun drawSliderBarRect(canvas: Canvas, start: Float, end: Float)
    {
        canvas.drawRect(start, 0f, end, sliderHeight, sliderBarPaint)
    }

    private fun mixColor(@FloatRange(from = 0.0, to = 1.0) sliderRatio: Float, colorArray: IntArray): Int
    {
        if (sliderRatio <= 0)
        {
            return colorArray.first()
        }
        else if (sliderRatio >= 1)
        {
            return colorArray.last()
        }

        var colorWeight = sliderRatio * (colorArray.size - 1)
        val startIndex = colorWeight.toInt()
        // Remove whole part of decimal
        colorWeight -= startIndex

        val startColor = colorArray[startIndex]
        val endColor = colorArray[startIndex + 1]

        val r = avg(red(startColor), red(endColor), colorWeight)
        val g = avg(green(startColor), green(endColor), colorWeight)
        val b = avg(blue(startColor), blue(endColor), colorWeight)

        return rgb(r, g, b)
    }


    private fun avg(start: Int, end: Int, @FloatRange(from = 0.0, to = 1.0) weight: Float)
            = start + Math.round(weight * (end - start))

    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        val action = event.action
        if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN)
        {

            xRatio = event.x / width
            displayText = true
            sliderBarCenterX = event.x.toInt()
        }
        else if (action == MotionEvent.ACTION_UP)
        {
            displayText = false
        }

        postInvalidateOnAnimation()

        return true
    }

    fun addSliderChangeListener(sliderListener: (Int) -> Unit)
    {
        sliderChangeListeners.add(sliderListener)
    }

    fun removeSliderChangeListener(sliderListener: (Int) -> Unit)
    {
        sliderChangeListeners.remove(sliderListener)
    }

    private fun notifySliderChangeListeners(value: Int)
    {
        for (changeListener in sliderChangeListeners)
        {
            changeListener.invoke(value)
        }
    }
}


