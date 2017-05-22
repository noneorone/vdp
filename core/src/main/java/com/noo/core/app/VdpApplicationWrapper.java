package com.noo.core.app;

import android.app.Application;

import java.lang.ref.SoftReference;

/**
 * Application的封装引用
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/20 16:02<br/>
 * @since 1.0
 */
public class VdpApplicationWrapper {

    private VdpApplicationWrapper() {
    }

    private static SoftReference<Application> app;

    public static final void wrap(Application application) {
        app = new SoftReference<>(application);
    }

    public static final Application get() {
        return app.get();
    }

}
