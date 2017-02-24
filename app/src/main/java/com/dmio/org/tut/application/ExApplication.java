package com.dmio.org.tut.application;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;
import com.facebook.stetho.InspectorModulesProvider;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Application Extension
 *
 * @author mars.wong noneorone@yeah.net
 * @since 2016/9/14 14:28
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
    }
}
