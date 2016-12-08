package com.dev.cameronc.hues.LightGroup

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.dev.cameronc.hues.R
import com.larswerkman.holocolorpicker.ColorPicker
import io.reactivex.Observable

/**
 * Created by ccord on 12/7/2016.
 */
class LightColorPickerDialog : DialogFragment()
{
    private lateinit var colorPicker: ColorPicker
    private lateinit var cancelButton: Button
    private lateinit var setColorButton: Button
    var colorPickerListener: ColorPickerListener? = null
        set(value)
        {
            field = value
            cancelButton.setOnClickListener { colorPickerListener?.onCancelPressed(arguments.getInt(INITIAL_COLOR_KEY)) }
            setColorButton.setOnClickListener {
                colorPickerListener?.onSetColorPressed(colorPicker.color)
                dismiss()
            }
        }
    private var attachListener: AttachListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val view = inflater.inflate(R.layout.color_picker_dialog, container, false)
        colorPicker = view.findViewById(R.id.dialog_color_picker) as ColorPicker
        cancelButton = view.findViewById(R.id.color_picker_dialog_cancel) as Button
        setColorButton = view.findViewById(R.id.color_picker_dialog_set) as Button

        val initialColor = arguments.getInt(INITIAL_COLOR_KEY)
        colorPicker.oldCenterColor = initialColor
        colorPicker.color = initialColor

        attachListener?.onColorDialogAttached()
        return view
    }

    fun createColorChangedObservable(picker: ColorPicker): Observable<Int>
    {
        return Observable.create<Int> { ev ->
            picker.onColorChangedListener = ColorPicker.OnColorChangedListener { color -> ev.onNext(color) }
        }
    }

    fun colorChangedObservable(): Observable<Int>
    {
        return createColorChangedObservable(colorPicker)
    }

    interface ColorPickerListener : ColorPicker.OnColorChangedListener, ColorPicker.OnColorSelectedListener
    {
        override fun onColorSelected(color: Int)
        override fun onColorChanged(color: Int)
        fun onCancelPressed(initialColor: Int)
        fun onSetColorPressed(color: Int)
    }

    fun setAttachListener(listener: AttachListener)
    {
        attachListener = listener
    }

    interface AttachListener
    {
        fun onColorDialogAttached()
    }

    companion object
    {
        val INITIAL_COLOR_KEY = "initialColor"

        fun newInstance(color: Int): LightColorPickerDialog
        {
            val pickerDialog = LightColorPickerDialog()
            val bundle = Bundle()
            bundle.putInt(INITIAL_COLOR_KEY, color)
            pickerDialog.arguments = bundle

            return pickerDialog
        }
    }
}
