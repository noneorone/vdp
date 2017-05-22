package com.dmio.org.tut.core;

import com.antfortune.freeline.FreelineCore;
import com.facebook.stetho.Stetho;
import com.noo.core.app.VdpApplication;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

/**
 * Application Extension
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2016/9/14 14:28<br/>
 * @since 1.0
 */
public class ExApplication extends VdpApplication {

    public ExApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Freeline
        FreelineCore.init(this);

        // Stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build()
        );

    }
}
