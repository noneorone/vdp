package com.noo.core.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.noo.core.R;


/**
 * 组件工具类，处理例如组件状态检测、組件装载等，组件包括{@link Fragment}、{@link FragmentActivity}等<br/>
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/3/1 9:39<br/>
 * @since 1.0
 */
public class ComponentUtils {

    /**
     * 检测FragmentActivity是否有效
     *
     * @param activity {@link FragmentActivity}对象
     * @return true有效, false无效
     */
    public static boolean isStateValid(Activity activity) {
        boolean isValid = activity != null && !activity.isFinishing();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isValid = isValid && !activity.isDestroyed();
        }
        return isValid;
    }

    /**
     * 检测Fragment是否有效
     *
     * @param fragment {@link Fragment}对象
     * @return true有效, false无效
     */
    public static boolean isStateValid(Fragment fragment) {
        if (fragment != null && !fragment.isDetached() && !fragment.isRemoving()) {
            return isStateValid(fragment.getActivity());
        }
        return false;
    }

    /**
     * 从宿主Fragment中创建Fragment的优化处理
     *
     * @param fragment 需要创建的{@link Fragment}
     * @param frameId  需要创建的{@link Fragment}的父级容器中位置id
     */
    public static void addSubFrament(Fragment parent, Fragment fragment, int frameId, Bundle bundle) {
        if (isStateValid(parent)) {
            FragmentManager manager = parent.getChildFragmentManager();
            FragmentTransaction beginTransaction = manager.beginTransaction();
            fragment.setArguments(bundle);
            beginTransaction.replace(frameId, fragment);
            beginTransaction.commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
    }


    /**
     * 从宿主FragmentActivity中创建Fragment的优化处理
     *
     * @param fragment     被操作的{@link Fragment}
     * @param hostActivity 宿主{@link FragmentActivity}
     * @param frameId      需要创建的{@link Fragment}的父级容器中位置id
     */
    public static void initFrament(Fragment fragment, FragmentActivity hostActivity, int frameId, Bundle bundle) {
        if (fragment != null && isStateValid(hostActivity)) {
            FragmentManager manager = hostActivity.getSupportFragmentManager();
            FragmentTransaction beginTransaction = manager.beginTransaction();
            fragment.setArguments(bundle);
            beginTransaction.replace(frameId, fragment);
            beginTransaction.commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
    }


    /**
     * 根据不同的状态进入不同界面
     *
     * @param srcFrm        源{@link Fragment}
     * @param targetFrm     目标{@link Fragment}
     * @param bundle        需要传递的参数
     * @param showAnimation 是否显示过渡动画
     * @param frameId       需要创建的{@link Fragment}的父级容器中位置id
     */
    public static void forwardEntrance(Fragment srcFrm, Fragment targetFrm, Bundle bundle, boolean showAnimation, int frameId) {
        if (isStateValid(srcFrm) && targetFrm != null) {
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
                transaction.replace(frameId, targetFrm);
                transaction.commitAllowingStateLoss();
                manager.executePendingTransactions();
            }
        }
    }


}
