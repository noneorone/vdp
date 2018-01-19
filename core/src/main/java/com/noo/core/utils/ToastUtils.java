package com.noo.core.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Toast显示<br/>
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2018/1/18 19:48<br/>
 * @since 1.0
 */
public class ToastUtils {

    /**
     * toast显示
     *
     * @param context
     * @param resId
     */
    public static void showToast(Context context, @StringRes int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

}
