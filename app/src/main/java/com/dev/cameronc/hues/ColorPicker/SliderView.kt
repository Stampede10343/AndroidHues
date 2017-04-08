package com.dev.cameronc.hues.ColorPicker

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.FloatRange
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.OverScroller
import com.dev.cameronc.hues.R
import com.dev.cameronc.hues.dpToPx
import java.util.*

/**
 * Created by ccord on 11/21/2016.
 */
class SliderView : View, GestureDetector.OnGestureListener
{
    private val defaultHeight: Int = dpToPx(32)
    private var sliderHeight: Float = 0f
    private var sliderBarWidth: Int = 0

    private val sliderBarPaint: Paint
    private var sliderBarCenterX: Int = 0

    private var displayText: Boolean = false

    private val gestureDetector: GestureDetector
    private val scroller: OverScroller
    private var catchupAnimator: ValueAnimator? = null

    private val sliderChangeListeners: MutableList<(Int) -> Unit> = ArrayList()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, @StyleRes styleAttr: Int) : super(context, attrs, styleAttr)
    constructor(context: Context, attrs: AttributeSet, @StyleRes styleAttr: Int, styleRes: Int) : super(context, attrs, styleAttr, styleRes)

    init
    {

        sliderBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            strokeWidth = sliderBarWidth / 2f
            color = Color.argb(120, 100, 100, 100)
        }

        val backgroundVal = TypedValue()
        context.theme.resolveAttribute(R.attr.background, backgroundVal, true)
        val backgroundColor = backgroundVal.data

        gestureDetector = GestureDetector(context, this)
        scroller = OverScroller(context)

        //setBackgroundColor(backgroundColor)

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

        sliderBarWidth = (h * 1.25).toInt()
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
            notifySliderChangeListeners(sliderRelativePosition())
        }

        if (!scroller.isFinished)
        {
            scroller.computeScrollOffset()
            sliderBarCenterX = scroller.currX
            postInvalidate()
        }
    }

    private fun sliderRelativePosition()
            = ((sliderBarCenterX / width.toFloat()) * 100).toInt()

    private fun sliderBarOffEndScreen()
            = sliderBarCenterX + sliderBarWidth / 2 > width

    private fun sliderBarOffStartScreen()
            = sliderBarCenterX - sliderBarWidth / 2 < 0

    private fun drawSliderBarRect(canvas: Canvas, start: Float, end: Float)
    {
        canvas.drawRect(start, 0f, end, sliderHeight, sliderBarPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean
    {
        return gestureDetector.onTouchEvent(event)
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

    /**
     * Set the slider to some position and notify listeners
     *
     * @param position The position from 0 - 1 relative
     */
    fun setSliderPosition(@FloatRange(from = 0.0, to = 1.0) position: Float)
    {
        if (width == 0)
        {
            return
        }

        val newPosition = (width * position).toInt()
        val animator: ValueAnimator = ValueAnimator.ofInt(sliderBarCenterX, newPosition)
        animator.apply {
            interpolator = DecelerateInterpolator()
            duration = 300
            addUpdateListener { anim ->
                sliderBarCenterX = anim.animatedValue as Int
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter()
            {
                override fun onAnimationEnd(animation: Animator?)
                {
                    notifySliderChangeListeners(sliderRelativePosition())
                }
            })
        }
        animator.start()

    }

    override fun onShowPress(e: MotionEvent)
    {
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean
    {
        return false
    }

    override fun onDown(e: MotionEvent): Boolean
    {
        scroller.forceFinished(false)
        catchupAnimator = ValueAnimator.ofInt(sliderBarCenterX, e.x.toInt())
        catchupAnimator!!.interpolator = DecelerateInterpolator()
        catchupAnimator!!.duration = 100
        catchupAnimator!!.addUpdateListener { pos ->
            sliderBarCenterX = pos.animatedValue as Int
            invalidate()
        }
        catchupAnimator!!.start()

        return true
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean
    {
        catchupAnimator?.cancel()
        scroller.fling(e2.x.toInt(), 0, (velocityX / 2).toInt(), 0, 0, width, 0, 0)
        postInvalidate()
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean
    {
        if (catchupAnimator?.isRunning ?: false)
        {
            catchupAnimator?.end()
            sliderBarCenterX -= distanceX.toInt()
            invalidate()
        }
        else
        {
            scroller.forceFinished(true)
            sliderBarCenterX -= distanceX.toInt()
            invalidate()
        }

        return true
    }

    override fun onLongPress(e: MotionEvent?)
    {
    }

    override fun onSaveInstanceState(): Parcelable
    {
        val viewState = SliderViewState(super.onSaveInstanceState())
        viewState.sliderPosition = sliderBarCenterX
        return viewState
    }

    override fun onRestoreInstanceState(state: Parcelable?)
    {
        super.onRestoreInstanceState(state)
        sliderBarCenterX = (state as SliderViewState).sliderPosition
    }

    class SliderViewState : BaseSavedState
    {
        var sliderPosition: Int = 0

        constructor(parcelable: Parcelable) : super(parcelable)
        constructor(parcel: Parcel) : super(parcel)
        {
            sliderPosition = parcel.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int)
        {
            super.writeToParcel(out, flags)
            out.writeInt(sliderPosition)
        }

        companion object SliderCreator
        {
            @JvmField val CREATOR: Parcelable.Creator<SliderViewState> = object : Parcelable.Creator<SliderViewState>
            {
                override fun newArray(size: Int): Array<SliderViewState?>
                {
                    return arrayOfNulls(size)
                }

                override fun createFromParcel(source: Parcel): SliderViewState
                {
                    return SliderViewState(source)
                }
            }
        }
    }

    fun setSliderPositionNoAnimation(ratio: Float)
    {
        post {
            sliderBarCenterX = (width * ratio).toInt()
            invalidate()
        }
    }
}


