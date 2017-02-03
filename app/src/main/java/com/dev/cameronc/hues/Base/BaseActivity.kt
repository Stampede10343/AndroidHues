package com.dev.cameronc.hues.Base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.dev.cameronc.hues.HuesApplication
import com.dev.cameronc.hues.ThemeManager
import com.philips.lighting.hue.sdk.PHHueSDK
import javax.inject.Inject

/**
 * Created by ccord on 11/9/2016.
 */
open class BaseActivity : AppCompatActivity()
{
    @Inject
    lateinit var hueSDK: PHHueSDK
    @Inject
    lateinit var themeManager: ThemeManager

    protected val app: HuesApplication
        get() = application as HuesApplication


    override fun onCreate(savedInstanceState: Bundle?)
    {
        app.getApplicationComponent().inject(this)
        setTheme(themeManager.getTheme())
        super.onCreate(savedInstanceState)
    }
}
