package com.dev.cameronc.hues.Connect

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dev.cameronc.hues.Model.AccessPoint
import com.dev.cameronc.hues.R
import java.util.*

/**
 * Created by ccord on 11/18/2016.
 */

internal class AccessPointDialog() : DialogFragment()
{
    private lateinit var accessPointList: RecyclerView
    private var accessPoints: MutableList<AccessPoint>? = null
    var apInteractor: AccessPointDialogInterator? = null

    init
    {
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val view = inflater!!.inflate(R.layout.access_point_dialog, container, false)

        accessPoints = arguments.getParcelableArrayList<AccessPoint>("APs")

        accessPointList = view.findViewById(R.id.access_point_recyclerview) as RecyclerView
        accessPointList.adapter = APRecyclerViewAdapter(accessPoints!!, apInteractor)
        accessPointList.layoutManager = LinearLayoutManager(view!!.context)

        return view
    }

    companion object
    {
        fun newInstance(accessPoints: ArrayList<AccessPoint>): AccessPointDialog
        {
            val apDialog = AccessPointDialog()
            val bundle = Bundle()
            bundle.putParcelableArrayList("APs", accessPoints)

            apDialog.arguments = bundle

            return apDialog
        }

    }

    class APRecyclerViewAdapter(val accessPoints: MutableList<AccessPoint>, val apInteractor: AccessPointDialogInterator?) : RecyclerView.Adapter<ApViewHolder>()
    {

        override fun getItemCount(): Int
        {
            return accessPoints.size
        }

        override fun onBindViewHolder(holder: ApViewHolder?, position: Int)
        {
            val ap = accessPoints[position]

            holder!!.icon.setImageResource(R.mipmap.ic_launcher)
            holder.ipAddress.text = ap.ipAddress
            holder.macAddress.text = ap.macAddress

            holder.itemView.setOnClickListener { apInteractor?.onApClicked(ap) }
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ApViewHolder
        {
            val view = LayoutInflater.from(parent!!.context).inflate(R.layout.ap_item, parent, false)

            return ApViewHolder(view)
        }
    }

    class ApViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        internal val icon: ImageView
        internal val ipAddress: TextView
        internal val macAddress: TextView

        init
        {
            icon = view.findViewById(R.id.ap_item_icon) as ImageView
            ipAddress = view.findViewById(R.id.ap_item_ip_address) as TextView
            macAddress = view.findViewById(R.id.ap_item_mac_address) as TextView
        }

    }

    interface AccessPointDialogInterator
    {
        fun onApClicked(accessPoint: AccessPoint)
    }

}
