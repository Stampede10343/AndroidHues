package com.dev.cameronc.hues.Home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.dev.cameronc.hues.ColorPicker.ColorPickerView
import com.dev.cameronc.hues.R
import com.philips.lighting.model.PHGroup

/**
 * Created by ccord on 11/20/2016.
 */
class LightGroupAdapter(val groupList: List<HueGroupInfo>) : RecyclerView.Adapter<LightGroupAdapter.LightGroupVH>()
{
    var lightGroupSliderListener: LightGroupSliderListener? = null
    var groupOnListener: LightGroupOnListener? = null

    override fun onBindViewHolder(holder: LightGroupVH, position: Int)
    {
        val hueGroupInfo = groupList[position]
        holder.groupNameTextView.text = hueGroupInfo.group.name.capitalize()

        holder.brightnessSlider.addSliderChangeListener { percent -> lightGroupSliderListener?.onSliderChanged(hueGroupInfo.group, percent) }

        holder.groupOnSwitch.isChecked = hueGroupInfo.on
        holder.groupOnSwitch.setOnCheckedChangeListener({ button, isOn ->
            groupOnListener?.onGroupOnToggled(hueGroupInfo.group, isOn)
            if (!isOn)
            {
                holder.brightnessSlider.setSliderPosition(0f)
            }
        })
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
        val groupOnSwitch: Switch

        init
        {
            groupNameTextView = view.findViewById(R.id.light_group_item_name) as TextView
            brightnessSlider = view.findViewById(R.id.light_group_item_slider) as ColorPickerView
            groupOnSwitch = view.findViewById(R.id.light_group_item_switch) as Switch
        }
    }

    interface LightGroupSliderListener
    {
        fun onSliderChanged(hueGroup: PHGroup, percent: Int)
    }

    interface LightGroupOnListener
    {
        fun onGroupOnToggled(phGroup: PHGroup, isOn: Boolean)
    }
}

data class HueGroupInfo(val group: PHGroup, val on: Boolean)
{

}


