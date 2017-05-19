package com.noo.core.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.noo.core.R;
import com.noo.core.widget.msv.MultiStateView;
import com.noo.core.widget.msv.MultiStateViewHelper;
import com.noo.core.widget.msv.ViewType;

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

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View container = getLayoutInflater().inflate(R.layout.vdp_container, null);

        tvTitle = (AppCompatTextView) container.findViewById(R.id.tv_title);
        toolbar = (Toolbar) container.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
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
