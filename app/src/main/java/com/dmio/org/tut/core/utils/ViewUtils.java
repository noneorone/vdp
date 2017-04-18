package com.dmio.org.tut.core.utils;

import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;

/**
 * 功能说明：视图工具类<br/>
 * 作者：wangmeng on 2017/3/29 19:56<br/>
 * 邮箱：noneorone@yeah.net
 */
public final class ViewUtils {

    private ViewUtils() {
    }

    /**
     * 获取指定id的view组件
     *
     * @param activity 活动项
     * @param id       view资源ID
     * @param <T>      指定类型的view
     * @return 返回指定组件对象view
     */
    public static <T extends View> T get(AppCompatActivity activity, @IdRes int id) {
        View view = null;

        boolean isActived = (activity != null && !activity.isFinishing());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isActived = isActived && !activity.isDestroyed();
        }
        if (isActived) {
            view = activity.findViewById(id);
        }

        return (T) view;
    }

    /**
     * 获取指定ViewHolder内的视图组件，一般用于List
     *
     * @param view
     * @param id
     * @param <T>
     * @return
     */
    public static <T extends View> T get(View view, @IdRes int id) {
        if (view == null) return null;
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }


}
