package com.dev.cameronc.hues.Home;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by ccord on 11/9/2016.
 */

public class HomePresenter implements HomeContract.Presenter<HomeContract.View>
{
    private final PHHueSDK hueSDK;
    private HomeContract.View view;

    public HomePresenter(PHHueSDK hueSDK)
    {
        this.hueSDK = hueSDK;
    }

    @Override
    public HomeContract.View getView()
    {
        return view;
    }

    @Override
    public void onViewAttached(HomeContract.View view)
    {
        this.view = view;

        List<PHBridge> bridges = hueSDK.getAllBridges();
        StringBuilder builder = new StringBuilder();
        for (PHBridge bridge : bridges)
        {
            builder.append(bridge.toString());
        }

        view.showBridges(builder.toString());
    }

    @Override
    public void onViewDetached()
    {
        view = null;
    }

    @Override
    public void setView(@Nullable HomeContract.View view)
    {
        this.view = view;
    }
}
