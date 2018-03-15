package noneorone.org.mvp.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * {@link Snackbar} utils
 * Created by wangmeng on 2018/3/14.
 */
public class SnackbarUtils {

    private SnackbarUtils() {
    }

    /**
     * show message
     *
     * @param view
     * @param msg
     */
    public static void show(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}
