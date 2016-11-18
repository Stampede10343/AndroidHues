package com.dev.cameronc.hues.Connect

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dev.cameronc.hues.Base.BaseActivity
import com.dev.cameronc.hues.R
import com.philips.lighting.hue.sdk.PHAccessPoint

/**
 * Created by ccord on 11/16/2016.
 */
class ConnectActivity : BaseActivity(), ConnectContract.View
{
    var connectProgressDialog: ProgressDialog? = null
    var presenter: ConnectContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.connect_dialog_view)

        presenter = ConnectPresenter()
        app.getApplicationComponent().inject(presenter as ConnectPresenter)
        presenter?.onViewAttached(this)
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