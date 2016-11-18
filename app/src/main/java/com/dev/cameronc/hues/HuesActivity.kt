package com.dev.cameronc.hues

import android.content.Intent
import android.os.Bundle

import com.dev.cameronc.hues.Base.BaseActivity
import com.dev.cameronc.hues.Connect.ConnectActivity

class HuesActivity : BaseActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val intent = Intent(this, ConnectActivity().javaClass)
        startActivity(intent)

    }

    override fun getSystemService(name: String): Any
    {
        if (name == "Dagger")
        {
            return app.getApplicationComponent()
        }
        else
        {
            return super.getSystemService(name)
        }
    }
}
