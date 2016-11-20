package com.dev.cameronc.hues.Connect

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by ccord on 11/19/2016.
 */
class BridgeAuthProgressView : View
{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, style: Int) : super(context, attrs, style)

    var centerX: Float = 0f
    var centerY: Float = 0f
    var bridgeRadius: Float = 0f
    var lightRadius: Float = 0f

    var circlePaint: Paint
    var lightPaint: Paint

    init
    {
        circlePaint = Paint()
        circlePaint.color = Color.WHITE
        circlePaint.isAntiAlias = true

        lightPaint = Paint()
        lightPaint.color = Color.rgb(30, 245, 255)
        lightPaint.isAntiAlias = true
        lightPaint.strokeWidth = 6f
        lightPaint.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        when (widthMode)
        {
            MeasureSpec.AT_MOST -> width = Math.min(width, DEFAULT_SIZE)
            MeasureSpec.UNSPECIFIED -> width = DEFAULT_SIZE
        }

        when (heightMode)
        {
            MeasureSpec.AT_MOST -> height = Math.min(height, DEFAULT_SIZE)
            MeasureSpec.UNSPECIFIED -> height = DEFAULT_SIZE
        }

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
    {
        super.onSizeChanged(w, h, oldw, oldh)

        // Deals with potential different padding will shift the center appropriately
        centerX = (width / 2f) + paddingStart - paddingEnd
        centerY = (height / 2f) + paddingTop - paddingBottom

        bridgeRadius = (width - paddingStart - paddingEnd) / 2f
        lightRadius = bridgeRadius * 0.3f
        lightPaint.strokeWidth = lightRadius * 0.2f
    }

    override fun onDraw(canvas: Canvas)
    {
        super.onDraw(canvas)

        canvas.drawCircle(centerX, centerY, bridgeRadius, circlePaint)
        canvas.drawCircle(centerX, centerY, lightRadius, lightPaint)
    }

    companion object
    {
        val DEFAULT_SIZE = 200
    }
}