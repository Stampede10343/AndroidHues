package com.dev.cameronc.hues.Home

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.dev.cameronc.hues.Base.BaseActivity
import com.dev.cameronc.hues.Connect.ConnectActivity
import com.dev.cameronc.hues.R
import com.philips.lighting.model.PHGroup
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_home.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeActivity : BaseActivity(), HomeContract.View
{
    @Inject lateinit var presenter: HomePresenter
    var subs = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        app.getHomeComponent().inject(this)
    }

    override fun onResume()
    {
        super.onResume()
        presenter.onViewAttached(this)
    }

    override fun onStop()
    {
        super.onStop()
        subs.clear()
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
        // Probably should release the component so the hue sdk listener doesn't get random events
        val intent = Intent(this, ConnectActivity::class.java)
        startActivity(intent)
    }

    override fun notifyBridgeConnected()
    {
        Snackbar.make(view_container, "Connected!", Snackbar.LENGTH_LONG).show()
    }

    override fun showLightGroups(allGroups: List<HueGroupInfo>)
    {
        val groupAdapter = LightGroupAdapter(allGroups)
        createSliderObservable(groupAdapter)
        groupAdapter.groupOnListener = object : LightGroupAdapter.LightGroupOnListener
        {
            override fun onGroupOnToggled(phGroup: PHGroup, isOn: Boolean)
            {
                presenter.onGroupOnToggled(phGroup, isOn)
            }
        }

        home_group_recyclerview.post {
            home_group_recyclerview.adapter = groupAdapter
            home_group_recyclerview.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun showNoLightGroups()
    {
        Snackbar.make(view_container, "No light groups found :(", Snackbar.LENGTH_INDEFINITE).show()
    }

    private fun createSliderObservable(adapter: LightGroupAdapter)
    {
        subs.add(Observable.create(ObservableOnSubscribe<GroupUpdateEvent> { subscriber ->
            val updateEvent = GroupUpdateEvent()
            adapter.lightGroupSliderListener = object : LightGroupAdapter.LightGroupSliderListener
            {
                override fun onSliderChanged(hueGroup: PHGroup, percent: Int)
                {
                    updateEvent.group = hueGroup
                    updateEvent.percent = percent
                    subscriber.onNext(updateEvent)
                }
            }
        }).debounce(20L, TimeUnit.MILLISECONDS).subscribe { ev -> presenter.onSliderChanged(ev) })

    }
}

class GroupUpdateEvent()
{
    var group: PHGroup = PHGroup()
    var percent = 0
}
