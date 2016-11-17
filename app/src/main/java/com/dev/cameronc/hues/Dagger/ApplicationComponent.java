package com.dev.cameronc.hues.Dagger;

import com.dev.cameronc.hues.Base.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ccord on 11/9/2016.
 */

@Singleton
@Component(modules = {AndroidModule.class, HueModule.class})
public interface ApplicationComponent
{
    void inject(BaseActivity activity);
}
