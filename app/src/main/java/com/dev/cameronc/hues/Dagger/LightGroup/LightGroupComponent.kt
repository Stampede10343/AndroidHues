package com.dev.cameronc.hues.Dagger.LightGroup

import com.dev.cameronc.hues.LightGroup.LightGroupActivity
import dagger.Subcomponent

/**
 * Created by ccord on 12/5/2016.
 */
@Subcomponent(modules = arrayOf(LightGroupModule::class))
interface LightGroupComponent
{
    fun inject(activity: LightGroupActivity)

}