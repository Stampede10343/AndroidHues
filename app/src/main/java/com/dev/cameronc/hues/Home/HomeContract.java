package com.dev.cameronc.hues.Home;

/**
 * Created by ccord on 11/9/2016.
 */

public interface HomeContract
{
    interface View
    {
        void showBridges(String bridges);
    }

    interface Presenter<V> extends com.dev.cameronc.hues.Base.Presenter<View>
    {

    }
}
