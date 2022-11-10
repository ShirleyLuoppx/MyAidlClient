package com.ppx.dailystudy.myaidlclient;

import android.app.Application;
import android.content.Context;

/**
 * @Author: LuoXia
 * @Date: 2022/3/15 16:18
 * @Description:
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }

    public static Context getContext() {
        if (mContext != null) {
            return mContext;
        } else {
            return App.getContext();
        }
    }
}
