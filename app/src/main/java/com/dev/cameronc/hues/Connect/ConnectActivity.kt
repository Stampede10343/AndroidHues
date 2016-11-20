package com.dev.cameronc.hues.Connect

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import android.widget.Toast
import com.dev.cameronc.hues.Base.BaseActivity
import com.dev.cameronc.hues.Home.HomeActivity
import com.dev.cameronc.hues.Model.AccessPoint
import com.dev.cameronc.hues.R
import com.philips.lighting.hue.sdk.PHAccessPoint
import java.util.*
import javax.inject.Inject

/**
 * Created by ccord on 11/16/2016.
 */
class ConnectActivity : BaseActivity(), ConnectContract.View, AccessPointDialog.AccessPointDialogInterator
{
    @Inject lateinit var presenter: ConnectContract.Presenter

    private var connectProgressDialog: ProgressDialog? = null
    private var accessPointsDialog: AccessPointDialog? = null

    private lateinit var authContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        authContainer = findViewById(R.id.connect_auth_container) as LinearLayout

        app.getConnectComponent().inject(this)
        presenter.onViewAttached(this)

        accessPointsDialog = supportFragmentManager.findFragmentByTag(AP_DIALOG_TAG) as? AccessPointDialog
        accessPointsDialog?.apInteractor = this

    }

    override fun onStop()
    {
        super.onStop()
        connectProgressDialog?.dismiss()

        presenter.onViewDetached()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        if (isFinishing)
        {
            Log.i(javaClass.name, "Connect Component released")
            presenter.onPresenterDestroyed()
            app.releaseConnectComponent()
        }
    }

    override fun showFindAPDialog()
    {
        connectProgressDialog = ProgressDialog(this)
        connectProgressDialog!!.isIndeterminate = true
        connectProgressDialog!!.setCancelable(false)
        connectProgressDialog!!.setMessage("Searching for Hue Bridges")
        connectProgressDialog!!.show()
    }

    override fun dismissFindApDialog()
    {
        connectProgressDialog?.dismiss()
    }

    override fun displayAccessPoints(accessPoints: MutableList<PHAccessPoint>)
    {
        val apNames: List<String> = accessPoints.map { ap -> ap.ipAddress }
        Log.i(javaClass.name, apNames.toString())

        val apList = accessPoints.map(::AccessPoint)

        accessPointsDialog = AccessPointDialog.newInstance(apList as ArrayList<AccessPoint>)
        accessPointsDialog!!.apInteractor = this
        accessPointsDialog!!.show(supportFragmentManager, AP_DIALOG_TAG)
    }

    override fun dismissAccessPointSelectionDialog()
    {
        accessPointsDialog?.dismiss()
    }

    override fun showAuthenticationScreen()
    {
        runOnUiThread {
            authContainer.alpha = 0f
            authContainer.visibility = View.VISIBLE
            authContainer.animate().alpha(1f).setInterpolator(AccelerateInterpolator()).setDuration(400).start()
        }
    }

    override fun showNoAccessPointsFound()
    {
        Toast.makeText(this, "No APs Found!", Toast.LENGTH_LONG).show()
    }

    override fun onApClicked(accessPoint: AccessPoint)
    {
        presenter.onApClicked(accessPoint)
    }

    override fun showHomeScreen()
    {
        val homeIntent = Intent(this, HomeActivity::class.java)
        homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(homeIntent)
        finish()
    }

    companion object
    {
        val AP_DIALOG_TAG: String = "ApDialog"
    }
}
