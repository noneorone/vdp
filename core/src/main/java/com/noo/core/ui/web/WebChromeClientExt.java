package com.noo.core.ui.web;

import android.content.Context;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.noo.core.utils.ComponentUtils;

/**
 * {@link WebChromeClient}扩展
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/26 15:19<br/>
 * @since 1.0
 */
public class WebChromeClientExt extends WebChromeClient {

    private final Context mContext;

    public WebChromeClientExt(Context context) {
        this.mContext = context;
    }

    /**
     * 检测组件是否为活动项，并依此来确定返回结果及其内部JsResult处理
     *
     * @param superFlag 父类调用标志位
     * @param result    js处理结果对象{@link JsResult}
     * @return
     */
    private boolean checkComponentActived(boolean superFlag, JsResult result) {
        if (ComponentUtils.isStateValid(mContext)) {
            return superFlag;
        } else {
            // 处理用户dialog的取消过后对调用者通知JsResult已完成
            if (result != null) {
                result.cancel();
            }
            return true;
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        boolean superFlag = super.onJsAlert(view, url, message, result);
        return checkComponentActived(superFlag, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        boolean superFlag = super.onJsConfirm(view, url, message, result);
        return checkComponentActived(superFlag, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        boolean superFlag = super.onJsPrompt(view, url, message, defaultValue, result);
        return checkComponentActived(superFlag, result);
    }

}
