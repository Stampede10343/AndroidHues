package com.dev.cameronc.hues.Preferences

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

/**
 * Created by ccord on 2/1/2017.
 */
class SettingsListAdapter(val preferenceList: List<Preference>) : BaseAdapter()
{

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val view : View
        if(convertView == null)
        {
            view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        }
        else
        {
            view = convertView
        }

        val textView = view.findViewById(android.R.id.text1) as TextView
        textView.text = getItem(position).primaryText

        val textView2 = view.findViewById(android.R.id.text2) as TextView
        textView2.text = getItem(position).secondaryText

        return view
    }

    override fun getCount(): Int = preferenceList.size

    override fun getItem(position: Int): Preference = preferenceList[position]

    override fun getItemId(position: Int): Long = position.toLong()

}