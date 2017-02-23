package com.dmio.org.tut.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.util.List;

/**
 * Application Utils
 */
public class AppUtils {

    private AppUtils() {
        throw new AssertionError();
    }

    /**
     * whether this process is named with processName
     *
     * @param context
     * @param processName
     * @return <ul>
     * return whether this process is named with processName
     * <li>if context is null, return false</li>
     * <li>if {@link ActivityManager#getRunningAppProcesses()} is null, return false</li>
     * <li>if one process of {@link ActivityManager#getRunningAppProcesses()} is equal to processName, return
     * true, otherwise return false</li>
     * </ul>
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (processInfoList == null || processInfoList.size() == 0) {
            return false;
        }

        for (RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo != null && processInfo.pid == pid
                    && ((processName == null ? processInfo.processName == null : processName.equals(processInfo.processName)))) {
                return true;
            }
        }
        return false;
    }

    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     *
     * @param context
     * @return if application is in background return true, otherwise return false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取manifest中配置的不同类型meta节点下面指定名称对应值
     *
     * @param context
     * @param clz     分为{@link Application} | {@link Service} | {@link BroadcastReceiver} | {@link Activity}
     * @param name
     * @return
     */
    public static final String getMetaData(Context context, Class<?> clz, String name) {
        String value = null;

        try {
            PackageItemInfo info = null;
            PackageManager manager = context.getPackageManager();

            if (clz == Application.class) {
                info = manager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            } else if (clz == Service.class) {
                info = manager.getServiceInfo(new ComponentName(context, clz), PackageManager.GET_META_DATA);
            } else if (clz == BroadcastReceiver.class) {
                info = manager.getReceiverInfo(new ComponentName(context, clz), PackageManager.GET_META_DATA);
            } else if (clz == Activity.class) {
                if (context instanceof Activity) {
                    boolean isActive = false;
                    Activity activity = (Activity) context;
                    if (activity != null && !activity.isFinishing()) {
                        isActive = true;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        isActive = isActive && !activity.isDestroyed();
                    }
                    if (isActive) {
                        info = manager.getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
                    }
                }
            }

            value = info.metaData.getString(name);
        } catch (Exception e) {
        }

        return TextUtils.isEmpty(value) ? "" : value;
    }

}
