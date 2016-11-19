package com.dev.cameronc.hues.Model

import android.os.Parcel
import android.os.Parcelable
import com.philips.lighting.hue.sdk.PHAccessPoint

/**
 * Created by ccord on 11/19/2016.
 */
data class AccessPoint(val hueAp: PHAccessPoint) : Parcelable
{
    var ipAddress: String
    var macAddress: String
    var bridgeId: String
    var username: String?

    init
    {
        ipAddress = hueAp.ipAddress
        macAddress = hueAp.macAddress
        bridgeId = hueAp.bridgeId
        username = hueAp.username
    }

    companion object
    {
        @JvmField val CREATOR: Parcelable.Creator<AccessPoint> = object : Parcelable.Creator<AccessPoint>
        {
            override fun createFromParcel(source: Parcel): AccessPoint = AccessPoint(source)
            override fun newArray(size: Int): Array<AccessPoint?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(PHAccessPoint())
    {
        ipAddress = source.readString()
        macAddress = source.readString()
        bridgeId = source.readString()
        username = source.readString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int)
    {
        dest?.writeString(ipAddress)
        dest?.writeString(macAddress)
        dest?.writeString(bridgeId)
        dest?.writeString(username)
    }
}