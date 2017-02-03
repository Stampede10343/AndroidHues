package com.dev.cameronc.hues.Preferences

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dev.cameronc.hues.Base.BaseActivity
import com.dev.cameronc.hues.Connect.ConnectActivity
import com.dev.cameronc.hues.R
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*
import javax.inject.Inject

class SettingsActivity : BaseActivity()
{
    @Inject
    lateinit var preferences: SharedPrefs
    private var bridgeForgotten = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        app.getApplicationComponent().inject(this)

        setContentView(R.layout.activity_settings)

        val preferenceList = ArrayList<Preference>()
        preferenceList.add(Preference("Reset bridge", "Reset the bridge to configure it again from scratch"))
        preferenceList.add(Preference("Theme", "Select either dark theme or light theme"))

        preference_listview.adapter = SettingsListAdapter(preferenceList)
        preference_listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when (position)
            {
                0 -> promptToResetBridge()
                1 -> selectTheme()
            }
        }

        bridgeForgotten = savedInstanceState?.getBoolean(BRIDGE_FORGOTTEN) ?: false
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        outState.putBoolean(BRIDGE_FORGOTTEN, bridgeForgotten)
    }

    private fun promptToResetBridge()
    {
        AlertDialog.Builder(this).setTitle("Forget Hue Bridge")
                .setMessage("Forgetting the bridge will will make it so you have to setup to app to reconnect to the bridge")
                .setPositiveButton("Confirm", { dialog, which -> forgetBridge() })
                .setNegativeButton("Cancel", null)
                .show()
    }

    private fun forgetBridge()
    {
        preferences.putString(PreferenceKeys.LAST_BRIDGE_USERNAME, "")
        preferences.putString(PreferenceKeys.LAST_BRIDGE_IP, "")
        preferences.putString(PreferenceKeys.LAST_BRIDGE_MAC_ADDRESS, "")

        bridgeForgotten = true
    }

    private fun selectTheme()
    {
        val themes = arrayOf("Dark", "Light")
        AlertDialog.Builder(this)
                .setTitle("Select Theme")
                .setAdapter(ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, themes), { dialog, index -> onThemeClicked(index)})
                .show()
    }

    private fun onThemeClicked(index: Int)
    {
        when(index)
        {
            0 -> themeManager.setTheme(true)
            1 -> themeManager.setTheme(false)
        }

        recreate()
    }

    override fun onBackPressed()
    {
        if (bridgeForgotten)
        {
            val connectIntent = Intent(this, ConnectActivity::class.java)
            connectIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(connectIntent)
            overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom)
        }
        else
        {
            super.onBackPressed()
        }
    }

    companion object
    {
        val BRIDGE_FORGOTTEN = "bridgeForgotten"
    }
}
