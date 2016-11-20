package com.dev.cameronc.hues.Connect

import com.dev.cameronc.hues.Model.AccessPoint
import com.dev.cameronc.hues.SharedPrefs
import com.philips.lighting.hue.sdk.PHAccessPoint
import com.philips.lighting.hue.sdk.PHBridgeSearchManager
import com.philips.lighting.hue.sdk.PHHueSDK
import com.philips.lighting.model.PHBridge
import org.junit.Before
import org.junit.Test
import org.mockito.Matchers
import org.mockito.Matchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*

/**
 * Created by ccord on 11/19/2016.
 */
class ConnectPresenterTest
{
    lateinit var connectPresenter: ConnectPresenter
    val mockHueSdk: PHHueSDK = mock(PHHueSDK::class.java)
    val sharedPrefs: SharedPrefs = mock(SharedPrefs::class.java)
    val mockView: ConnectContract.View = mock(ConnectContract.View::class.java)

    val hueAp: PHAccessPoint = PHAccessPoint()

    @Before
    fun setUp()
    {
        connectPresenter = ConnectPresenter(mockHueSdk, sharedPrefs)
    }

    @Test
    fun onViewAttachedFirstTime()
    {
        val searchManager = mock(PHBridgeSearchManager::class.java)
        `when`(mockHueSdk.getSDKService(anyByte())).thenReturn(searchManager)

        connectPresenter.onViewAttached(mockView)

        verify(mockView).showFindAPDialog()
        verify(searchManager).search(true, true)
    }

    @Test
    fun onViewAttachedAfterInitialAuthCallback()
    {
        connectPresenter.accessPoints.add(AccessPoint(hueAp))
        connectPresenter.onAuthenticationRequired(hueAp)

        connectPresenter.onViewAttached(mockView)

        verify(mockHueSdk).startPushlinkAuthentication(Matchers.any(PHAccessPoint::class.java))
        verify(mockView, Mockito.times(1)).showAuthenticationScreen()
    }


    @Test
    fun onApClickedNoSelectedBridge()
    {
        connectPresenter.onApClicked(AccessPoint(hueAp))

        verify(mockHueSdk).connect(hueAp)
    }

    @Test
    fun onApClickedWithSelectedBridgeSet()
    {
        val mockBridge = mock(PHBridge::class.java)
        `when`(mockHueSdk.selectedBridge).thenReturn(mockBridge)

        connectPresenter.onApClicked(AccessPoint(hueAp))

        verify(mockHueSdk).disableHeartbeat(mockBridge)
        verify(mockHueSdk).disconnect(mockBridge)
        verify(mockHueSdk).connect(hueAp)
    }

    @Test
    fun onAccessPointsFound()
    {
        val accessPoints = getOneAccessPoint()
        connectPresenter.findingAps = true

        connectPresenter.onViewAttached(mockView)
        connectPresenter.onAccessPointsFound(accessPoints)

        verify(mockView).dismissFindApDialog()
        assert(connectPresenter.accessPoints.contains(AccessPoint(hueAp)))
        verify(mockView).displayAccessPoints(accessPoints)
    }

    private fun getOneAccessPoint(): ArrayList<PHAccessPoint>
    {
        val accessPoints = ArrayList<PHAccessPoint>()
        accessPoints.add(hueAp)
        return accessPoints
    }

    @Test
    fun onNoAccessPointsFound()
    {
        val accessPoints = ArrayList<PHAccessPoint>()
        connectPresenter.findingAps = true

        connectPresenter.onViewAttached(mockView)
        connectPresenter.onAccessPointsFound(accessPoints)

        verify(mockView).dismissFindApDialog()
        verifyZeroInteractions(mockHueSdk)
        verify(mockView).showNoAccessPointsFound()
    }

    @Test
    fun onAuthenticationRequired()
    {
        setPresenterAlreadyFoundAPs()

        connectPresenter.onViewAttached(mockView)
        val accessPoint = PHAccessPoint()
        connectPresenter.onAuthenticationRequired(accessPoint)

        verify(mockView).dismissAccessPointSelectionDialog()
        verify(mockHueSdk).startPushlinkAuthentication(accessPoint)
        verify(mockView).showAuthenticationScreen()
    }

    @Test
    fun onBridgeConnected()
    {
        setPresenterAlreadyFoundAPs()
        val bridge = mock(PHBridge::class.java)
        val username = "testUsername"
        connectPresenter.onViewAttached(mockView)

        connectPresenter.onBridgeConnected(bridge, username)

        verify(mockHueSdk).selectedBridge = bridge
        verify(mockHueSdk).enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL.toLong())

        // Figure out how to verify we're actually putting the right keys..
        //verify(sharedPrefs.putString(Matchers.eq(PreferenceKeys.LAST_BRIDGE_IP), anyString()))
        //verify(sharedPrefs.putString(eq(PreferenceKeys.LAST_BRIDGE_MAC_ADDRESS), anyString()))
        //verify(sharedPrefs.putString(eq(PreferenceKeys.LAST_BRIDGE_USERNAME), username))
        verify(sharedPrefs, Mockito.times(3)).putString(anyString(), anyString())
        verify(mockView).showHomeScreen()
    }

    private fun setPresenterAlreadyFoundAPs()
    {
        connectPresenter.findingAps = false
        connectPresenter.onAccessPointsFound(getOneAccessPoint())
    }

}