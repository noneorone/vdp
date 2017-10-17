package com.dmio.org.tut.activity.demo.webview;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.dmio.org.tut.R;

import java.text.MessageFormat;

public class WebViewTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static String JS_FUNC_NATIVE2JS = "native2Js('%s')";
    private static String JS_OBJECT_NAME = "BRIDGE";

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
        mWebView = (WebView) findViewById(R.id.wv_content);
        etContent = (EditText) findViewById(R.id.et_content);
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
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
     * 加载网页到webview
     */
    private void loadView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/native_js_pass_values.html");
        mWebView.addJavascriptInterface(new JsBridge(), JS_OBJECT_NAME);
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
