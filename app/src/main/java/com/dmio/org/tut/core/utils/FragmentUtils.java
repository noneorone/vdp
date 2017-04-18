package com.dmio.org.tut.core.utils;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dmio.org.tut.R;

/**
 * 功能说明：Fragment相关操作
 * 作者：wangmeng on 2017/3/1 9:39
 * 邮箱：noneorone@yeah.net
 */

public class FragmentUtils {

    /**
     * 从宿主Fragment中创建Fragment的优化处理
     *
     * @param fragment
     * @param frameLayoutResId
     */
    public static void addSubFrament(Fragment parent, Fragment fragment, int frameLayoutResId, Bundle bundle) {
        if (parent == null) return;

        boolean isActive = !parent.isDetached() && !parent.isRemoving();
        if (isActive) {
            FragmentManager manager = parent.getChildFragmentManager();
            FragmentTransaction beginTransaction = manager.beginTransaction();
            fragment.setArguments(bundle);
            beginTransaction.replace(frameLayoutResId, fragment);
            beginTransaction.commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
    }


    /**
     * 从宿主FragmentActivity中创建Fragment的优化处理
     *
     * @param fragment
     * @param hostActivity
     * @param frameLayoutResId
     */
    public static void initFrament(Fragment fragment, FragmentActivity hostActivity, int frameLayoutResId, Bundle bundle) {
        if (fragment == null || hostActivity == null) return;

        boolean isActive = !hostActivity.isFinishing();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isActive = (!hostActivity.isFinishing() && !hostActivity.isDestroyed());
        }

        if (isActive) {
            FragmentManager manager = hostActivity.getSupportFragmentManager();
            FragmentTransaction beginTransaction = manager.beginTransaction();
            fragment.setArguments(bundle);
            beginTransaction.replace(frameLayoutResId, fragment);
            beginTransaction.commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
    }


    /**
     * 根据不同的状态进入不同界面
     *
     * @param srcFrm
     * @param targetFrm
     * @param bundle
     */
    public static void forwardEntrance(Fragment srcFrm, Fragment targetFrm, Bundle bundle, boolean showAnimation, int contentAreaResId) {
        if (targetFrm != null && srcFrm != null) {
            FragmentActivity activity = srcFrm.getActivity();
            if (activity != null) {
                boolean isActive = !activity.isFinishing();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    isActive = (isActive && !activity.isDestroyed());
                }

                if (isActive) {
                    FragmentManager manager = null;
                    if (srcFrm.getParentFragment() != null) {
                        manager = srcFrm.getParentFragment().getChildFragmentManager();
                    } else {
                        manager = srcFrm.getChildFragmentManager();
                    }
                    if (manager != null) {
                        FragmentTransaction transaction = manager.beginTransaction();
                        targetFrm.setArguments(bundle);
                        if (showAnimation) {
                            transaction.setCustomAnimations(
                                    R.anim.common_push_in_right_anim,
                                    R.anim.common_push_out_left_anim,
                                    R.anim.common_push_in_left_anim,
                                    R.anim.common_push_out_right_anim
                            );
                        }
                        transaction.replace(contentAreaResId, targetFrm);
                        transaction.commitAllowingStateLoss();
                        manager.executePendingTransactions();
                    }
                }
            }
        }
    }


}
