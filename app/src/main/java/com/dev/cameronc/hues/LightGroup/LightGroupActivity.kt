package com.dev.cameronc.hues.LightGroup

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.dev.cameronc.hues.Base.BaseActivity
import com.dev.cameronc.hues.Home.HomeActivity
import com.dev.cameronc.hues.R
import com.philips.lighting.model.PHLight
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_light_group.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LightGroupActivity : BaseActivity(), LightGroupContract.View
{
    @Inject
    lateinit var presenter: LightGroupPresenter
    lateinit var groupId: String
    var subs = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_group)
        app.getLightGroupComponent().inject(this)
        restoreState(savedInstanceState)

        presenter.groupId = groupId
    }

    private fun restoreState(savedInstanceState: Bundle?)
    {
        if (savedInstanceState == null)
        {
            groupId = intent.getStringExtra(HomeActivity.GroupKey)
        }
        else
        {
            groupId = savedInstanceState.getString(HomeActivity.GroupKey)
        }
    }

    override fun onResume()
    {
        super.onResume()
        presenter.onViewAttached(this)
    }

    override fun onStop()
    {
        super.onStop()
        presenter.onViewDetached()
    }

    override fun onSaveInstanceState(outState: Bundle?)
    {
        super.onSaveInstanceState(outState)
        outState?.putString(HomeActivity.GroupKey, groupId)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        if (isFinishing)
        {
            app.releaseLightGroupComponent()
        }
    }

    override fun showLights(lights: List<PHLight>?)
    {
        if (lights != null)
        {
            val lightAdapter = LightAdapter(lights, object : LightAdapter.LightInteractionListener
            {
                override fun onSwitchToggled(light: PHLight, on: Boolean)
                {
                    presenter.onSwitchToggled(light, on)
                }
            })

            light_recyclerview.post {
                light_recyclerview.adapter = lightAdapter
                light_recyclerview.layoutManager = LinearLayoutManager(this)
            }

            // We don't want to send update events until the view is settled.
            // Not sure why this isn't an issue in the HomeActivity since it works the same
            light_recyclerview.postDelayed({ createSliderObservable(lightAdapter) }, 200)
        }

    }

    private fun createSliderObservable(adapter: LightAdapter)
    {
        subs.add(Observable.create(ObservableOnSubscribe<LightUpdateEvent> { subscriber ->
            val updateEvent = LightUpdateEvent()
            adapter.sliderListener = object : LightAdapter.LightSliderListener
            {

                override fun onLightSliderChanged(light: PHLight, value: Int)
                {
                    updateEvent.light = light
                    updateEvent.value = value
                    subscriber.onNext(updateEvent)
                }
            }
        }).debounce(20L, TimeUnit.MILLISECONDS).subscribe { ev -> presenter.onSliderChanged(ev) })
    }

    class LightUpdateEvent
    {
        lateinit var light: PHLight
        var value: Int = 0
    }
}