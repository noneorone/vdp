package com.dmio.org.tut.application;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;
import com.dmio.org.tut.core.ExceptionHandler;
import com.facebook.stetho.Stetho;
import com.noo.core.log.LoggerRecorder;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;

/**
 * Application Extension
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2016/9/14 14:28<br/>
 * @since 1.0
 */
public class ExApplication extends Application {

    private static ExApplication instance;

    public static ExApplication getInstance() {
        return instance;
    }

    public ExApplication() {
        super();
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // init logger recorder
        LoggerRecorder.init(this);

        // Freeline
        FreelineCore.init(this);

        // Reaml
        Realm.init(this);

        // Stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build()
        );

        // set default uncaught exception handler for gloabal error that can be stored in log files.
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

    @Override
    public void onTerminate() {
        LoggerRecorder.releaseResource();
        super.onTerminate();
    }
}
