package com.dev.cameronc.hues.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.dev.cameronc.hues.Base.BaseActivity
import com.dev.cameronc.hues.Connect.ConnectActivity
import com.dev.cameronc.hues.R
import javax.inject.Inject

class HomeActivity : BaseActivity(), HomeContract.View
{
    @Inject lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        app.getHomeComponent().inject(this)
        presenter.onViewAttached(this)
    }

    override fun onStop()
    {
        super.onStop()
        presenter.onViewDetached()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        if (isFinishing)
        {
            Log.i(javaClass.name, "Home component released")
            presenter.onPresenterDestroyed()
            app.releaseHomeComponent()
        }
    }

    override fun navigateToConnectScreen()
    {
        val intent = Intent(this, ConnectActivity::class.java)
        startActivity(intent)
    }
}
