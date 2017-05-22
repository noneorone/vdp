package com.noo.core.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import com.noo.core.log.Logger;
import com.noo.core.utils.AppUtils;

/**
 * 异常处理类
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/3/31 22:14<br/>
 * @since 1.0
 */
public class VdpCrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String EXTRAS_IS_CRASH = "is_crash";

    private static VdpCrashHandler mCrashHandler;

    private Context mContext;

    private long restartInterval = 1000L;

    private Class<?> restartComponentCls;

    private final Thread.UncaughtExceptionHandler mDefaultHandler;

    public static final VdpCrashHandler init(Context context, long restartInterval, Class<?> restartComponentCls) {
        VdpCrashHandler crashHandler = init(context, restartComponentCls);
        crashHandler.restartInterval = restartInterval;
        return crashHandler;
    }

    public static final VdpCrashHandler init(Context context, Class<?> restartComponentCls) {
        VdpCrashHandler crashHandler = init(context);
        crashHandler.restartComponentCls = restartComponentCls;
        return crashHandler;
    }

    public static final VdpCrashHandler init(Context context) {
        if (mCrashHandler == null) {
            synchronized (VdpCrashHandler.class) {
                if (mCrashHandler == null) {
                    mCrashHandler = new VdpCrashHandler(context);
                }
            }
        }

        return mCrashHandler;
    }

    private VdpCrashHandler(Context context) {
        this.mContext = context;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        if (!hasHandledException(e) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(t, e);
        } else {
            // 记录异常日志
            Logger.e(e);

            // 若重启的组件类没有指定，则默认为应用的启动页
            if (restartComponentCls == null) {
                ResolveInfo resolveInfo = AppUtils.getLauncher(mContext);
                if (resolveInfo != null) {
                    String className = resolveInfo.activityInfo.name;
                    try {
                        restartComponentCls = Class.forName(className);
                    } catch (ClassNotFoundException cnfe) {
                        Logger.e(cnfe);
                    }
                }
            }

            // 定时重启应用
            if (restartComponentCls != null) {
                AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

                Intent intent = new Intent(mContext, restartComponentCls);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(EXTRAS_IS_CRASH, true);
                PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + restartInterval, restartIntent);
            }

            // 退出应用
            VdpActivityManager.getInstance().finishAll();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            System.gc();

        }
    }

    /**
     * 监测是否已经处理了该异常信息
     *
     * @param e {@link Throwable}异常信息对象
     * @return 处理了该异常信息则返回true，否则返回false
     */
    private boolean hasHandledException(Throwable e) {
        return !(e == null);
    }

}
