package com.noo.core.app;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.noo.core.log.LoggerRecorder;
import com.noo.core.utils.FontUtils;

import io.realm.Realm;

/**
 * Application Extension
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2016/9/14 14:28<br/>
 * @since 1.0
 */
public class VdpApplication extends Application {

    private static VdpApplication instance;

    public static VdpApplication getInstance() {
        return instance;
    }

    public VdpApplication() {
        super();
        instance = this;
        VdpApplicationWrapper.wrap(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // init logger recorder
        LoggerRecorder.init(this);

        // set default uncaught exception handler for gloabal error that can be stored in log files.
        VdpCrashHandler.init(this);

        // Reaml
        Realm.init(this);

        // set font
        FontUtils.setFont(this, "SERIF", "font/Roboto-Regular.ttf");
        FontUtils.setFont(this, "DEFAULT", "font/Roboto-Regular.ttf");
        FontUtils.setFont(this, "MONOSPACE", "font/Roboto-Regular.ttf");
        FontUtils.setFont(this, "SANS_SERIF", "font/Roboto-Regular.ttf");
    }

    @Override
    public void onTerminate() {
        LoggerRecorder.releaseResource();
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1) {
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        if (!autoFontScale() && resources.getConfiguration().fontScale != 1) {
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();
            resources.updateConfiguration(newConfig, resources.getDisplayMetrics());
        }
        return resources;
    }

    /**
     * 默认不根据系统设置字体改变而变化
     *
     * @return
     */
    protected boolean autoFontScale() {
        return false;
    }

}
