package com.dev.cameronc.hues.Home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dev.cameronc.hues.ColorPicker.ColorPickerView
import com.dev.cameronc.hues.R
import com.philips.lighting.model.PHGroup

/**
 * Created by ccord on 11/20/2016.
 */
class LightGroupAdapter(val groupList: MutableList<PHGroup>) : RecyclerView.Adapter<LightGroupAdapter.LightGroupVH>()
{
    var sliderListener: SliderChangedListener? = null

    override fun onBindViewHolder(holder: LightGroupVH, position: Int)
    {
        holder.groupNameTextView.text = groupList[position].name.capitalize()

        holder.brightnessSlider.addSliderChangeListener { percent -> sliderListener?.onSliderChanged(groupList[position], percent) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightGroupVH
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.light_group_item, parent, false)

        return LightGroupVH(view)
    }

    override fun getItemCount(): Int
    {
        return groupList.size
    }

    class LightGroupVH(view: View) : RecyclerView.ViewHolder(view)
    {
        val groupNameTextView: TextView
        val brightnessSlider: ColorPickerView

        init
        {
            groupNameTextView = view.findViewById(R.id.light_group_item_name) as TextView
            brightnessSlider = view.findViewById(R.id.light_group_item_slider) as ColorPickerView
        }
    }

    interface SliderChangedListener
    {
        fun onSliderChanged(hueGroup: PHGroup, percent: Int)
    }
}


