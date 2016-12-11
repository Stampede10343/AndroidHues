package com.dev.cameronc.hues.LightGroup

import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.hue.sdk.PHNotificationManager
import com.philips.lighting.model.*
import io.reactivex.disposables.Disposable
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito.*

/**
 * Created by ccord on 12/11/2016.
 */
class LightGroupPresenterTest
{
    lateinit var presenter: LightGroupPresenter
    val hueSdk: PHHueSDK = mock(PHHueSDK::class.java)
    val view: LightGroupContract.View = mock(LightGroupContract.View::class.java)

    @Before
    fun setUp()
    {
        `when`(hueSdk.notificationManager).thenReturn(mock(PHNotificationManager::class.java))
        `when`(hueSdk.selectedBridge).thenReturn(mock(PHBridge::class.java))
        `when`(hueSdk.selectedBridge.resourceCache).thenReturn(mock(PHBridgeResourcesCache::class.java))
        presenter = LightGroupPresenter(hueSdk)
    }

    @Test
    fun onViewAttachedLightsNull()
    {
        val dummyGroup = PHGroup("dummy", "1")
        `when`(hueSdk.selectedBridge.resourceCache.allGroups).thenReturn(listOf(dummyGroup))
        presenter.groupId = "1"
        presenter.onViewAttached(view)

        verify(view).showLights(Matchers.anyListOf(PHLight::class.java))
        Assert.assertSame(presenter.group, dummyGroup)
    }

    @Test
    fun onViewAttachedLightsAlreadyFound()
    {
        val listOfOneLight = listOf(testLight())
        presenter.lights = listOfOneLight
        presenter.onViewAttached(view)

        verify(view).showLights(Matchers.eq(listOfOneLight))
    }

    private fun testLight()
            = PHLight("name", "id", "1", "model")

    @Test
    fun onViewDetachedClearsSubs()
    {
        presenter.subscriptions.add(mock(Disposable::class.java))
        presenter.onViewAttached(view)
        presenter.onViewDetached()

        Assert.assertEquals("Expected no subscriptions after view is detached", 0, presenter.subscriptions.size())
    }

    @Test
    fun onSwitchToggledOn()
    {
        presenter.onViewAttached(view)

        val lightToSwitch = testLight()
        presenter.onSwitchToggled(lightToSwitch, true)

        val expectedLightState = PHLightState()
        expectedLightState.isOn = true
        verify(hueSdk.selectedBridge).updateLightState(Matchers.eq(lightToSwitch), Matchers.eq(expectedLightState))
    }

    @Test
    fun onLightClicked()
    {
        presenter.onViewAttached(view)

        val light = testLight()
        presenter.onLightClicked(light)

        verify(view).showLightColorPicker(Matchers.any() ?: light)
    }

    @Test
    fun onLightColorChanged()
    {
        presenter.onViewAttached(view)
        val testLight = testLight()
        presenter.currentLight = testLight

        presenter.onLightColorChanged(1)


        verify(hueSdk.selectedBridge).updateLightState(Matchers.eq(testLight), Matchers.any())
    }

    @Test
    fun onLightColorSelected()
    {
        presenter.onViewAttached(view)
        val testLight = testLight()
        testLight.lastKnownLightState = PHLightState()
        testLight.lastKnownLightState.isOn = true
        presenter.currentLight = testLight

        presenter.onLightColorSelected(1)

        verify(hueSdk.selectedBridge).updateLightState(Matchers.eq(testLight), Matchers.any())
        //verify(view).updateListLightColor(Matchers.eq(Observable.just(1)) ?: Observable.just(1), Matchers.eq(testLight) ?: testLight)
    }

}