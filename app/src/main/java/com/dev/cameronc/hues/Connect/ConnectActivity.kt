package com.dev.cameronc.hues.Connect

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dev.cameronc.hues.Base.BaseActivity
import com.dev.cameronc.hues.Dagger.ConnectModule
import com.dev.cameronc.hues.Model.AccessPoint
import com.dev.cameronc.hues.R
import com.philips.lighting.hue.sdk.PHAccessPoint
import java.util.*
import javax.inject.Inject

/**
 * Created by ccord on 11/16/2016.
 */
class ConnectActivity : BaseActivity(), ConnectContract.View
{
    @Inject lateinit var presenter: ConnectPresenter

    private var connectProgressDialog: ProgressDialog? = null
    private var accessPointsDialog: AccessPointDialog? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connect_dialog_view)

        app.getApplicationComponent().plus(ConnectModule()).inject(this)
        presenter.onViewAttached(this)
    }

    override fun onStop()
    {
        super.onStop()
        presenter.onViewDetached()
    }

    override fun showFindAPDialog()
    {
        connectProgressDialog = ProgressDialog(this)
        connectProgressDialog!!.isIndeterminate = true
        connectProgressDialog!!.setCancelable(false)
        connectProgressDialog!!.setMessage("Searching for Hue Bridges")
        connectProgressDialog!!.show()
    }

    override fun dismissAPDialog()
    {
        connectProgressDialog?.dismiss()
    }

    override fun displayAccessPoints(accessPoints: MutableList<PHAccessPoint>)
    {
        val apNames: List<String> = accessPoints.map { ap -> ap.ipAddress }
        Log.i(javaClass.name, apNames.toString())

        val apList = accessPoints.map(::AccessPoint)

        accessPointsDialog = AccessPointDialog.newInstance(apList as ArrayList<AccessPoint>)
        accessPointsDialog!!.show(supportFragmentManager, "ApDialog")
    }

    override fun showAuthenticationDialog(accessPoint: PHAccessPoint?)
    {
        Toast.makeText(this, "Need auth!", Toast.LENGTH_LONG).show()
    }

    override fun showNoAccessPointsFound()
    {
        Toast.makeText(this, "No APs Found!", Toast.LENGTH_LONG).show()
    }
}