package com.noo.core.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.noo.core.R;
import com.noo.core.app.VdpActivityManager;
import com.noo.core.app.VdpApplication;
import com.noo.core.ui.msv.MultiStateView;
import com.noo.core.ui.msv.MultiStateViewHelper;
import com.noo.core.ui.msv.ViewType;
import com.noo.core.utils.topbar.TranslucentUtils;

/**
 * 扩展{@link AppCompatActivity}基础类
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/15 16:35<br/>
 * @since 1.0
 */
public abstract class VdpActivity extends AppCompatActivity implements VdpComponent {

    private Toolbar toolbar;
    private AppCompatTextView tvTitle;
    private MultiStateView multiStateView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBarSetting();
        VdpActivityManager.getInstance().push(this);
    }

    @Override
    protected void onDestroy() {
        VdpActivityManager.getInstance().remove(this);
        super.onDestroy();
        VdpApplication.getRefWatcher(this).watch(this);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        tvTitle.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        tvTitle.setText(titleId);
    }

    /**
     * 隐藏ActionBar，默认不隐藏
     *
     * @return true表示隐藏，false表示不隐藏
     */
    protected boolean hideActionBar() {
        return false;
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View container = getLayoutInflater().inflate(R.layout.vdp_container, null);

        tvTitle = (AppCompatTextView) container.findViewById(R.id.title);
        toolbar = (Toolbar) container.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (hideActionBar()) {
                actionBar.hide();
            }
            actionBar.setHomeAsUpIndicator(R.drawable.action_bar_back);
            actionBar.setDisplayHomeAsUpEnabled(displayHomeAsUpEnabled());
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        View contentView = getLayoutInflater().inflate(layoutResID, null);

        multiStateView = (MultiStateView) container.findViewById(R.id.msv);

        MultiStateViewHelper.setAttrs(
                multiStateView,
                contentView,
                getLoadingLayoutId(),
                getErrorLayoutId(),
                getEmptyLayoutId(),
                MultiStateView.VIEW_STATE_CONTENT,
                animateViewStateChange()
        );

        super.setContentView(container);
    }

    /**
     * 初始化顶部栏位控件(包括顶部状态栏和{@link ActionBar})的配置，主要是设置过渡颜色
     */
    protected void initTopBarSetting() {
        //透明状态栏
//        TranslucentUtils.setStatusBar(this, getTopNavBarDrawable(), true);
        // 设定actionbar背景
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(getTopNavBarDrawable());
        }
    }

    /**
     * 供不同界面顶部导航栏与状态栏样式设置重载
     *
     * @return {@link Drawable}
     */
    protected Drawable getTopNavBarDrawable() {
        return ContextCompat.getDrawable(this, R.drawable.gradient_c17_bg);
    }

    /**
     * 注册点击返回控件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean displayHomeAsUpEnabled() {
        return true;
    }

    @Override
    public Object onBackgroundProcess(String tag, Object... params) {
        return null;
    }

    @Override
    public void onResultSuccess(String tag, Object obj) {
    }

    @Override
    public void onResultError(String tag, Object obj) {
    }

    @Override
    public int getLoadingLayoutId() {
        return R.layout.msv_loading;
    }

    @Override
    public int getErrorLayoutId() {
        return R.layout.msv_error;
    }

    @Override
    public int getEmptyLayoutId() {
        return R.layout.msv_empty;
    }

    @Override
    public MultiStateView getMultiStateView() {
        return multiStateView;
    }

    @Override
    public boolean animateViewStateChange() {
        return true;
    }

    @Override
    public void setView(View view, boolean switchToState, ViewType viewType) {
        multiStateView.setViewForState(view, viewType.getValue(), switchToState);
    }

    @Override
    public void showView(ViewType viewType) {
        multiStateView.setViewState(viewType.getValue());
    }
}
