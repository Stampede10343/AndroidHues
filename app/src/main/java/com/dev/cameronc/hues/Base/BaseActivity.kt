package com.dev.cameronc.hues.Base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.dev.cameronc.hues.HuesApplication
import com.philips.lighting.hue.sdk.PHHueSDK

import javax.inject.Inject

/**
 * Created by ccord on 11/9/2016.
 */
open class BaseActivity : AppCompatActivity()
{
    @Inject
    lateinit var hueSDK: PHHueSDK

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //app.getApplicationComponent().inject(this)
    }

    protected val app: HuesApplication
        get() = application as HuesApplication
}
