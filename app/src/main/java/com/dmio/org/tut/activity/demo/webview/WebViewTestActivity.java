package com.dmio.org.tut.activity.demo.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dmio.org.tut.R;

import java.text.MessageFormat;

public class WebViewTestActivity extends AppCompatActivity implements View.OnClickListener {

    // 方式一
    private static String JS_FUNC_NATIVE2JS = "native2Js('%s')";
    private static String JS_OBJECT_NAME = "BRIDGE";

    // 方式二
    private static String JS_SCHEMA = "js";
    private static String JS_AUTHORITY = "webview";
    private static String JS_PARAMS = "params";

    private LinearLayout llWeb;
    private WebView mWebView;
    private EditText etContent;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_test);
        initView();
        loadView();
    }

    private void initView() {
        llWeb = (LinearLayout) findViewById(R.id.ll_web);
        etContent = (EditText) findViewById(R.id.et_content);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        // 动态加入WebView
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mWebView = new WebView(getApplicationContext());
        llWeb.addView(mWebView, lp);
    }

    @Override
    protected void onDestroy() {
        // 先清除WebView以免内存泄露
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String native_msg = etContent.getText().toString();
                evaluateJavascript(mWebView, String.format(JS_FUNC_NATIVE2JS, native_msg));
                break;
            default:
                break;
        }
    }

    /**
     * 解析Uri
     *
     * @param uri
     * @return
     */
    private boolean parseUri(Uri uri, JsPromptResult result) {
        boolean flag = false;
        // 一般根据协议格式(schema)和协议名(authority)判断，如url="js:webview?arg1=111&arg2=222"约定
        if (uri != null && JS_SCHEMA.equals(uri.getScheme())) {
            if (JS_AUTHORITY.equals(uri.getAuthority())) {
                String params = uri.getQueryParameter(JS_PARAMS);
                etContent.setText(params);

                flag = true;
            }
        }

        if (result != null) {
            result.confirm("success? " + flag);
        }

        return flag;
    }

    /**
     * 加载网页到webview
     */
    private void loadView() {
        // 启用chrome远程调试
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        // 移除可能存在漏洞的远程调用的js接口
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
        // 启用js交互调用
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/native_js_pass_values.html");

        // 方式一：简单但4.2版本以下存在严重的漏洞问题（可通过js注入来调用java反射到指定的类如Runtime类从而执行本地代码）
        mWebView.addJavascriptInterface(new JsBridge(), JS_OBJECT_NAME);

        // 方式二：不存在方式一的漏洞，但是要将处理过后的值回传给js的话只能在js中单独定义接收结果的函数并通过webview.loadUrl()的方式去回传
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return parseUri(Uri.parse(url), null);
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return parseUri(request.getUrl(), null);
            }
        });

        // 方式三：拦截js的prompt框，因为它可返回任意类型而操作方便，能满足大多数互调场景，但使用起来比较复杂
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return parseUri(Uri.parse(message), result);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }

            @Override
            public boolean onJsTimeout() {
                return super.onJsTimeout();
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                String msg = String.format("{level: %s, sourceId: %s, lineNumber: %s, message: %s}", cm.messageLevel(), cm.sourceId(), cm.lineNumber(), cm.message());
                switch (cm.messageLevel()) {
                    case TIP:
                    case LOG:
                    case DEBUG:
                        Log.d("console", msg);
                        break;
                    case WARNING:
                        Log.w("console", msg);
                        break;
                    case ERROR:
                        Log.e("console", msg);
                        break;
                    default:
                        ;
                }
                return super.onConsoleMessage(cm);
            }
        });

    }

    /**
     * Native调用JS
     *
     * @param webView
     * @param pureJsCode
     */
    @SuppressLint("NewApi")
    public static void evaluateJavascript(final WebView webView, String pureJsCode) {
        String jsCodeFormat = "javascript:{0}";
        final String jsCode = MessageFormat.format(jsCodeFormat, pureJsCode);
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        webView.evaluateJavascript(jsCode, null);
                    } else {
                        webView.loadUrl(jsCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * JS调用Native的桥接对象
     */
    public class JsBridge {

        @JavascriptInterface
        public void js2Native(final String params) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    etContent.setText(params);
                }
            });
        }

    }


}
