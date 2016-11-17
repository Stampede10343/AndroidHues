package com.dev.cameronc.hues

import android.os.Bundle

import com.dev.cameronc.hues.Base.BaseActivity

class HuesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }

    override fun getSystemService(name: String): Any {
        if (name == "Dagger") {
            return app.applicationComponent
        }
        else {
            return super.getSystemService(name)
        }
    }
}
