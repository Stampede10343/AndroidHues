package com.dev.cameronc.hues.Connect

import android.os.Bundle
import android.os.PersistableBundle
import com.dev.cameronc.hues.Base.BaseActivity

/**
 * Created by ccord on 11/16/2016.
 */
class ConnectActivity : BaseActivity(), ConnectContract.View {

    var presenter: ConnectContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun showConnectDialog() {
    }
}