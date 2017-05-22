package com.noo.core.app;

import android.app.Activity;

import com.noo.core.utils.ComponentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity Manager
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/22 10:18<br/>
 * @since 1.0
 */
public class VdpActivityManager {

    private final List<Activity> activities;


    private static VdpActivityManager activityManager;

    public static final VdpActivityManager getInstance() {
        if (activityManager == null) {
            synchronized (VdpActivityManager.class) {
                if (activityManager == null) {
                    activityManager = new VdpActivityManager();
                }
            }
        }
        return activityManager;
    }

    private VdpActivityManager() {
        activities = new ArrayList<>();
    }

    /**
     * 存储指定{@link Activity}
     *
     * @param activity 继承自{@link Activity}的activity对象
     * @return true 成功，失败 false
     */
    public boolean push(final Activity activity) {
        if (!activities.contains(activity)) {
            return activities.add(activity);
        }
        return false;
    }

    /**
     * 删除指定{@link Activity}
     *
     * @param activity 继承自{@link Activity}的activity对象
     * @return true 成功，失败 false
     */
    public boolean remove(final Activity activity) {
        if (activities.contains(activity)) {
            return activities.remove(activity);
        }
        return false;
    }

    /**
     * 结束所有记录的{@link Activity}
     */
    public void finishAll() {
        if (!activities.isEmpty()) {
            for (Activity activity : activities) {
                if (ComponentUtils.isStateValid(activity)) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 获取最顶上的{@link Activity}
     *
     * @return 返回最顶上的{@link Activity}
     */
    public Activity getTopActivity(boolean containsTop) {
        int lastIndex = activities.size() - 1;
        if (!containsTop) {
            lastIndex--;
        }
        for (int i = lastIndex; i >= 0; i--) {
            Activity activity = activities.get(i);
            if (activity.isFinishing()) {
                continue;
            }
            return activity;
        }
        return activities.get(lastIndex);
    }


}
