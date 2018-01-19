package com.noo.core.utils.topbar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 透明状态栏工具类
 * Created by huangx on 2017/9/4.
 */
public class TranslucentUtils {
    /**
     * 透明状态栏
     *
     * @param activity
     * @param statusBarBg 状态栏背景
     * @param fitSystem   是否适配系统UI
     */
    public static void setStatusBar(Activity activity, Drawable statusBarBg, boolean fitSystem) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4以上和可以透明状态栏
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0换一种方式（全透明）
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
            ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
            ViewGroup rootView = decorViewGroup.findViewById(android.R.id.content);
            if (rootView != null) {
                View contentView = rootView.getChildAt(0);
                if (contentView != null) {
                    contentView.setFitsSystemWindows(fitSystem);
                }
                //用来设置状态样背景色
                View statusBarTintView = rootView.getChildAt(1);
                if (statusBarTintView == null) {
                    statusBarTintView = new View(activity);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
                    params.gravity = Gravity.TOP;
                    statusBarTintView.setLayoutParams(params);
                    rootView.addView(statusBarTintView);
                }
                statusBarTintView.setBackground(statusBarBg);
            }
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}
