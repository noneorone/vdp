package com.dmio.org.tut.application;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;

/**
 * 扩展Application
 *
 * @author mars.wong noneorone@yeah.net
 * @since 2016/9/14 14:28
 */

public class ExApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FreelineCore.init(this);
    }
}
