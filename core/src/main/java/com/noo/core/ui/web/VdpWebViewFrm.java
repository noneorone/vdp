package com.noo.core.ui.web;

import android.Manifest;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.noo.core.utils.permission.PermChecker;
import com.noo.core.utils.permission.PermRequestActivity;

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
    private boolean receivedError;

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
        progressBar = view.findViewById(R.id.pb_loading);
        if (mWebView == null) {
            mWebView = new WebView(getContext().getApplicationContext());
            mWebView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            mWebView.setBackgroundResource(R.color.c6);
            view.addView(mWebView);
        }
        return view;
    }

    @Override
    public boolean animateViewStateChange() {
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initArguments();
        initWebView();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            ViewParent parent = mWebView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeAllViews();
            }
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    private void initArguments() {
        Bundle extras = getArguments();
        if (extras != null) {
            url = extras.getString(EXTRA_URL);
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

        // 禁用部分可能存在外部调用风险的属性和js接口调用配置
        settings.setSavePassword(false);
        settings.setAllowFileAccess(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }
        mWebView.removeJavascriptInterface("accessibility");
        mWebView.removeJavascriptInterface("accessibilityTraversal");
        mWebView.removeJavascriptInterface("searchBoxJavaBridge_");

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
        MultiStateView multiStateView = getMultiStateView();
        View errorView = multiStateView.getView(MultiStateView.VIEW_STATE_ERROR);
        TextView tvMessage = errorView.findViewById(R.id.tv_message);
        Button btnRetry = errorView.findViewById(R.id.btn_retry);
        tvMessage.setText(errorCode + " : " + description);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receivedError = false;
                reload();
            }
        });
        int width = multiStateView.getWidth();
        int height = multiStateView.getHeight();
        errorView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
        showView(ViewType.ERROR);
        receivedError = true;
    }

    /**
     * 初始化{@link WebView}组件
     */
    private void initWebView() {
        initSettings();
        setClient();
        setChromeClient();
        setDownloadManager();

        mWebView.requestFocusFromTouch();
        mWebView.loadUrl(url);
    }

    /**
     * 扩展{@link WebViewClient}
     */
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

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    /**
     * 扩展{@link WebChromeClient}
     */
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
                    showView(receivedError ? ViewType.ERROR : ViewType.CONTENT);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });
    }

    /**
     * 设置文件下载监听处理
     */
    private void setDownloadManager() {
        final VdpDownloadManager.Mode downloadMode = VdpDownloadManager.Mode.USER_DEFINED;
        if (VdpDownloadManager.Mode.USER_DEFINED.equals(downloadMode)) {
            String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            PermRequestActivity.requestPermissions(getContext(), perms, new PermChecker.RequestPermCallback() {
                @Override
                public void onRequestSuccess() {
                    mWebView.setDownloadListener(new VdpDownloadManager(getContext(), downloadMode));
                }

                @Override
                public void onRequestFail() {
                }
            });
        } else {
            mWebView.setDownloadListener(new VdpDownloadManager(getContext(), downloadMode));
        }
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


