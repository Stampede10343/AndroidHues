package com.dev.cameronc.hues.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dev.cameronc.hues.HuesApplication;
import com.philips.lighting.hue.sdk.PHHueSDK;

import javax.inject.Inject;

/**
 * Created by ccord on 11/9/2016.
 */
public class BaseActivity extends AppCompatActivity
{
    @Inject
    protected PHHueSDK hueSDK;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getApp().getApplicationComponent().inject(this);
    }

    protected HuesApplication getApp()
    {
        return ((HuesApplication) getApplication());
    }
}
