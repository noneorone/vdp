package com.noo.core.ui.web;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.noo.core.R;
import com.noo.core.log.Logger;
import com.noo.core.ui.VdpFragment;
import com.noo.core.ui.msv.MultiStateView;
import com.noo.core.ui.msv.ViewType;
import com.noo.core.utils.ComponentUtils;
import com.noo.core.utils.DeviceUtils;

/**
 * 包含{@link WebView}封装{@link VdpFragment}
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/25 15:32<br/>
 * @since 1.0
 */
public class VdpWebViewFrm extends VdpFragment {

    public static final String EXTRA_URL = "_extra_url";

    private ProgressBar progressBar;
    private WebView mWebView;

    private String url;

    public WebView getWebView() {
        return mWebView;
    }

    private void setTitle(String title) {
        if (ComponentUtils.isStateValid(this)) {
            VdpWebViewAct act = (VdpWebViewAct) getActivity();
            act.setTitle(title);
        }
    }

    @Override
    public View getContentView() {
        ViewGroup view = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.vdp_empty_web, null);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        mWebView = new WebView(getContext().getApplicationContext());
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        mWebView.setBackgroundResource(R.color.c4);
        view.addView(mWebView);
        return view;
    }

    @Override
    public boolean animateViewStateChange() {
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMultiStateView().setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        getMultiStateView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.c1));
        initArguments();
        initWebView();
    }

    private void initArguments() {
        Bundle extras = getArguments();
        if (extras != null) {
            url = extras.getString(EXTRA_URL) + "sdfsfdsfsdfsdfsdfdsfdsf";
        }
    }

    /**
     * 初始化配置
     */
    private void initSettings() {
        WebSettings settings = mWebView.getSettings();
        // 将图片调整到适合webview的大小
        settings.setUseWideViewPort(true);
        // 缩放到屏幕大小
        settings.setLoadWithOverviewMode(true);

        // 支持js
        settings.setJavaScriptEnabled(true);
        // 设置编码格式
        settings.setDefaultTextEncodingName("utf-8");
        // 不使用缓存，只从网络获取数据
        if (DeviceUtils.isActiveNetConnected(getContext())) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        // 支持缩放
        settings.setSupportZoom(true);
        // 禁用内置缩放控件
        settings.setBuiltInZoomControls(false);
        // 隐藏原生缩放控件
        settings.setDisplayZoomControls(false);

        // 支持多窗口
        settings.supportMultipleWindows();
        // 设置通过JS打开新窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        // 支持內容重新布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // 设置可以访问文件
        settings.setAllowFileAccess(true);
        // 设置自动加载图片
        settings.setLoadsImagesAutomatically(true);

        // 启用dom存储
        settings.setDomStorageEnabled(true);
        // 启用数据库存储
        settings.setDatabaseEnabled(true);
        // 启用应用缓存
        settings.setAppCacheEnabled(true);

    }

    /**
     * 显示错误页面
     *
     * @param errorCode
     * @param description
     */
    private void showErrorView(int errorCode, CharSequence description) {
        View errorView = getMultiStateView().getView(MultiStateView.VIEW_STATE_ERROR);
        TextView tvMessage = (TextView) errorView.findViewById(R.id.tv_message);
        Button btnRetry = (Button) errorView.findViewById(R.id.btn_retry);
        tvMessage.setText(errorCode + " : " + description);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
        showView(ViewType.ERROR);
    }

    /**
     * 初始化{@link WebView}组件
     */
    private void initWebView() {
        initSettings();
        setClient();
        setChromeClient();

        mWebView.requestFocusFromTouch();
        mWebView.loadUrl(url);
    }

    private void setClient() {
        mWebView.setWebViewClient(new WebViewClientExt() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                showErrorView(error.getErrorCode(), error.getDescription());
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                showErrorView(errorCode, description);
            }
        });
    }

    private void setChromeClient() {
        mWebView.setWebChromeClient(new WebChromeClientExt(getContext()) {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Logger.d("newProgress: " + newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
    }


    /**
     * 回退历史
     *
     * @return true可回退，false不可回退
     */
    public boolean goBack() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    /**
     * 重新加载页面
     */
    public void reload() {
        if (mWebView != null) {
            mWebView.reload();
        }
    }

}


