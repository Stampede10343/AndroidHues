package com.dev.cameronc.hues.Home

import com.dev.cameronc.hues.Preferences.PreferenceKeys
import com.dev.cameronc.hues.Preferences.SharedPrefs
import com.philips.lighting.hue.sdk.PHAccessPoint
import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.hue.sdk.PHNotificationManager
import com.philips.lighting.model.*
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*

/**
 * Created by ccord on 12/14/2016.
 */
class HomePresenterTest
{
    private lateinit var homePresenter: HomePresenter
    private lateinit var view: HomeContract.View
    private lateinit var hueSdk: PHHueSDK
    private lateinit var sharedPrefs: FakeSharedPrefs

    @Before
    fun setUp()
    {
        view = mock(HomeContract.View::class.java)
        hueSdk = mock(PHHueSDK::class.java)
        Mockito.`when`(hueSdk.notificationManager).thenReturn(mock(PHNotificationManager::class.java))
        sharedPrefs = FakeSharedPrefs()
        homePresenter = HomePresenter(hueSdk, sharedPrefs)
    }

    @Test
    fun onViewAttachedNotConnectedNoPreferencesGoToConnectScreen()
    {
        homePresenter.onViewAttached(view)

        Assert.assertThat("Last bridge IP was not what was expected",
                sharedPrefs.retrievedValues[PreferenceKeys.LAST_BRIDGE_IP] as String, equalTo(""))
        Assert.assertThat("Last bridge MAC was not what was expected",
                sharedPrefs.retrievedValues[PreferenceKeys.LAST_BRIDGE_MAC_ADDRESS] as String, equalTo(""))
        Assert.assertThat("Last bridge username was not what was expected",
                sharedPrefs.retrievedValues[PreferenceKeys.LAST_BRIDGE_USERNAME] as String, equalTo(""))

        verify(hueSdk.notificationManager).unregisterSDKListener(homePresenter)
        verify(view).navigateToConnectScreen()
    }

    @Test
    fun onViewAttachedNotConnectedHasPreferencesDoesConnect()
    {
        setPreferences()

        homePresenter.onViewAttached(view)

        Assert.assertThat("Last bridge IP was not what was expected",
                sharedPrefs.retrievedValues[PreferenceKeys.LAST_BRIDGE_IP] as String, equalTo(IP))
        Assert.assertThat("Last bridge MAC was not what was expected",
                sharedPrefs.retrievedValues[PreferenceKeys.LAST_BRIDGE_MAC_ADDRESS] as String, equalTo(MAC))
        Assert.assertThat("Last bridge username was not what was expected",
                sharedPrefs.retrievedValues[PreferenceKeys.LAST_BRIDGE_USERNAME] as String, equalTo(USERNAME))

        verify(hueSdk).connect(Matchers.any(PHAccessPoint::class.java))
    }

    @Test
    fun onViewAttachedPresenterConnectedFalseShowsLights()
    {
        setPreferences()

        setupOneGroupWithOneLight()

        homePresenter.onViewAttached(view)

        Assert.assertEquals("Presenter was not marked connected", true, homePresenter.connected)
        verify(view).showLightGroups(Matchers.anyListOf(GroupAdapter.HueGroupInfo::class.java))
    }

    private fun setupOneGroupWithOneLight()
    {
        `when`(hueSdk.isAccessPointConnected(Matchers.any(PHAccessPoint::class.java))).thenReturn(true)
        `when`(hueSdk.selectedBridge).thenReturn(mock(PHBridge::class.java))
        `when`(hueSdk.selectedBridge.resourceCache).thenReturn(mock(PHBridgeResourcesCache::class.java))
        val group = PHGroup("name", "id")
        group.lightIdentifiers = listOf("lightKey")
        `when`(hueSdk.selectedBridge.resourceCache.allGroups).thenReturn(listOf(group))

        val light = createLightWithLastKnownState()
        `when`(hueSdk.selectedBridge.resourceCache.lights).thenReturn(mapOf(Pair("lightKey", light)))
    }

    @Test
    fun onViewAttachedPresenterConnectedFalseShowsNoLights()
    {
        setPreferences()

        `when`(hueSdk.isAccessPointConnected(Matchers.any(PHAccessPoint::class.java))).thenReturn(true)
        `when`(hueSdk.selectedBridge).thenReturn(mock(PHBridge::class.java))
        `when`(hueSdk.selectedBridge.resourceCache).thenReturn(mock(PHBridgeResourcesCache::class.java))
        val group = PHGroup("name", "id")
        group.lightIdentifiers = listOf("lightKey")
        `when`(hueSdk.selectedBridge.resourceCache.allGroups).thenReturn(emptyList())

        homePresenter.onViewAttached(view)

        verify(view).showNoLightGroups()
    }

    @Test
    fun onViewAttachedPresenterConnectedTrue()
    {
        setPreferences()

        `when`(hueSdk.isAccessPointConnected(Matchers.any(PHAccessPoint::class.java))).thenReturn(true)
        `when`(hueSdk.selectedBridge).thenReturn(mock(PHBridge::class.java))
        `when`(hueSdk.selectedBridge.resourceCache).thenReturn(mock(PHBridgeResourcesCache::class.java))
        val group = PHGroup("name", "id")
        group.lightIdentifiers = listOf("lightKey")
        `when`(hueSdk.selectedBridge.resourceCache.allGroups).thenReturn(emptyList())

        homePresenter.connected = true

        homePresenter.onViewAttached(view)

        verify(view).showLightGroups(Matchers.anyListOf(GroupAdapter.HueGroupInfo::class.java))
    }

    @Test
    fun onViewDetachedViewIsNull()
    {
        homePresenter.onViewAttached(view)

        homePresenter.onViewDetached()
        Assert.assertNull(homePresenter.view)
    }

    @Test
    fun onPresenterDestroyedUnregistersSdkListener()
    {
        homePresenter.onPresenterDestroyed()

        verify(hueSdk.notificationManager).unregisterSDKListener(Matchers.eq(homePresenter))
    }

    @Test
    fun onBridgeConnected()
    {
        val bridgeToConnectTo = mock(PHBridge::class.java)
        `when`(bridgeToConnectTo.resourceCache).thenReturn(mock(PHBridgeResourcesCache::class.java))
        val group = PHGroup()
        group.lightIdentifiers = listOf("id")
        `when`(bridgeToConnectTo.resourceCache.allGroups).thenReturn(listOf(group))
        val light = createLightWithLastKnownState()
        `when`(bridgeToConnectTo.resourceCache.lights).thenReturn(mapOf(Pair("id", light)))
        `when`(hueSdk.selectedBridge).thenReturn(mock(PHBridge::class.java))
        `when`(hueSdk.selectedBridge.resourceCache).thenReturn(mock(PHBridgeResourcesCache::class.java))
        `when`(hueSdk.selectedBridge.resourceCache.lights).thenReturn(mapOf(Pair("id", light)))

        homePresenter.onViewAttached(view)
        homePresenter.onBridgeConnected(bridgeToConnectTo, USERNAME)

        // TODO: Make a fake bridge class
        //Assert.assertSame("Selected bridge is not the connected bridge", hueSdk.selectedBridge, bridgeToConnectTo)
        Assert.assertEquals("Presenter should be connected", true, homePresenter.connected)
        verify(view).notifyBridgeConnected()
    }

    @Test
    fun onSliderChanged()
    {
        val mockBridge = mock(PHBridge::class.java)
        `when`(hueSdk.selectedBridge).thenReturn(mockBridge)
        `when`(hueSdk.selectedBridge.resourceCache).thenReturn(mock(PHBridgeResourcesCache::class.java))
        val light = mock(PHLight::class.java)
        `when`(light.lastKnownLightState).thenReturn(PHLightState())
        `when`(light.identifier).thenReturn("id")
        `when`(hueSdk.selectedBridge.resourceCache.lights).thenReturn(mapOf(Pair("id", light)))

        homePresenter.onViewAttached(view)

        val event = GroupUpdateEvent()
        val group = mock(PHGroup::class.java)
        `when`(group.lightIdentifiers).thenReturn(listOf("id"))
        event.group = group
        event.percent = 50

        homePresenter.onSliderChanged(event)

        verify(hueSdk.selectedBridge, Mockito.times(1)).updateLightState(eq(light), Matchers.any())
    }

    @Test
    fun onGroupOnToggled()
    {
        val mockBridge = mock(PHBridge::class.java)
        `when`(hueSdk.selectedBridge).thenReturn(mockBridge)
        `when`(hueSdk.selectedBridge.resourceCache).thenReturn(mock(PHBridgeResourcesCache::class.java))
        val light = mock(PHLight::class.java)
        `when`(light.lastKnownLightState).thenReturn(PHLightState())
        `when`(light.identifier).thenReturn("id")
        `when`(hueSdk.selectedBridge.resourceCache.lights).thenReturn(mapOf(Pair("id", light)))

        homePresenter.onViewAttached(view)

        val event = GroupUpdateEvent()
        val group = mock(PHGroup::class.java)
        `when`(group.lightIdentifiers).thenReturn(listOf("id"))
        event.group = group
        event.percent = 50

        homePresenter.onGroupOnToggled(group, true)

        verify(hueSdk.selectedBridge, Mockito.times(1)).updateLightState(Matchers.any(), Matchers.any())
    }

    @Test
    fun onGroupClicked()
    {
        homePresenter.onViewAttached(view)

        val hueGroup = PHGroup()
        homePresenter.onGroupClicked(hueGroup)

        verify(view).navigateToGroupScreen(Matchers.eq(hueGroup) ?: hueGroup)
    }

    private fun setPreferences()
    {
        sharedPrefs.putString(PreferenceKeys.LAST_BRIDGE_IP, IP)
        sharedPrefs.putString(PreferenceKeys.LAST_BRIDGE_MAC_ADDRESS, MAC)
        sharedPrefs.putString(PreferenceKeys.LAST_BRIDGE_USERNAME, USERNAME)
    }

    private fun createLightWithLastKnownState(): PHLight
    {
        val light = PHLight("name", "id", "1", "model")
        light.lastKnownLightState = PHLightState()
        return light
    }

    companion object Constants
    {
        val MAC = "aa:aa:aa:aa:aa:aa"
        val USERNAME = "username"
        val IP = "0.0.0.0"
    }

    class FakeSharedPrefs : SharedPrefs
    {
        val preferenceMap = HashMap<String, Any>()
        val retrievedValues = HashMap<String, Any>()

        override fun getBoolean(key: String): Boolean
        {
            val value = preferenceMap[key] as? Boolean ?: false
            retrievedValues.put(key, value)
            return value
        }

        override fun getInt(key: String): Int
        {
            val value = preferenceMap[key] as? Int ?: -1
            retrievedValues.put(key, value)
            return value
        }

        override fun getString(key: String): String
        {
            val value = preferenceMap[key] as? String ?: ""
            retrievedValues.put(key, value)
            return value
        }

        override fun putBoolean(key: String, value: Boolean)
        {
            preferenceMap[key] = value
        }

        override fun putInt(key: String, value: Int)
        {
            preferenceMap[key] = value
        }

        override fun putString(key: String, value: String)
        {
            preferenceMap[key] = value
        }
    }

}