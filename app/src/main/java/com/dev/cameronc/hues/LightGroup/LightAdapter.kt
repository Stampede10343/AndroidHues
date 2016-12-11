package com.dev.cameronc.hues.LightGroup

import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dev.cameronc.hues.Home.GroupAdapter
import com.dev.cameronc.hues.HueUtils
import com.dev.cameronc.hues.R
import com.dev.cameronc.hues.getColor
import com.philips.lighting.hue.sdk.utilities.PHUtilities
import com.philips.lighting.model.PHLight

/**
 * Created by ccord on 12/5/2016.
 */
class LightAdapter(val lights: List<PHLight>, val lightInteractionListener: LightInteractionListener) : RecyclerView.Adapter<GroupAdapter.LightGroupVH>()
{
    var sliderListener: LightSliderListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupAdapter.LightGroupVH
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.light_group_item, parent, false)
        return GroupAdapter.LightGroupVH(view)
    }

    override fun onBindViewHolder(holder: GroupAdapter.LightGroupVH, position: Int)
    {
        val lightItem = lights[position]
        holder.groupNameTextView.text = lightItem.name.capitalize()

        holder.brightnessSlider.addSliderChangeListener { v -> sliderListener?.onLightSliderChanged(lightItem, (254f * (v / 100f)).toInt()) }

        holder.groupOnSwitch.isChecked = lightItem.lastKnownLightState.isOn

        if (lightItem.lastKnownLightState.isOn)
        {
            val colorArray = HueUtils.createColorArray(lightItem.lastKnownLightState)
            val color = PHUtilities.colorFromXY(colorArray, lightItem.modelNumber)
            holder.groupIcon.imageTintList = ColorStateList.valueOf(color)

            holder.brightnessSlider.post {
                holder.brightnessSlider.setSliderPosition(lightItem.lastKnownLightState.brightness / 254f)
            }
        }
        else
        {
            holder.brightnessSlider.setSliderPosition(0f)
        }

        holder.groupOnSwitch.setOnCheckedChangeListener { button, isOn ->
            lightInteractionListener.onSwitchToggled(lightItem, isOn)
            if(isOn)
            {
                holder.brightnessSlider.setSliderPosition(lightItem.lastKnownLightState.brightness / 254f)
                holder.groupIcon.imageTintList = ColorStateList.valueOf(lightItem.getColor())
            }
            else
            {
                holder.brightnessSlider.setSliderPosition(0f)
                holder.groupIcon.imageTintList = ColorStateList.valueOf(Color.GRAY)
            }
        }

        holder.itemView.setOnClickListener { lightInteractionListener.onLightClicked(lightItem) }
    }

    override fun getItemCount(): Int = lights.size

    interface LightInteractionListener
    {
        fun onSwitchToggled(light: PHLight, on: Boolean)
        fun onLightClicked(lightItem: PHLight)
    }

    interface LightSliderListener
    {
        fun onLightSliderChanged(light: PHLight, value: Int)
    }

}
