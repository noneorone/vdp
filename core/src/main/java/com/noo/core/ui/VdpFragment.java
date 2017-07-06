package com.noo.core.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noo.core.R;
import com.noo.core.app.VdpApplication;
import com.noo.core.ui.msv.MultiStateView;
import com.noo.core.ui.msv.MultiStateViewHelper;
import com.noo.core.ui.msv.ViewType;

/**
 * 扩展{@link Fragment}基础类
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/25 20:06<br/>
 * @since 1.0
 */
public abstract class VdpFragment extends Fragment implements VdpComponent {

    private MultiStateView multiStateView;

    public abstract View getContentView();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        multiStateView = MultiStateViewHelper.attachView(
                getContentView(),
                getLoadingLayoutId(),
                getErrorLayoutId(),
                getEmptyLayoutId(),
                MultiStateView.VIEW_STATE_CONTENT,
                animateViewStateChange()
        );
        showView(ViewType.CONTENT);
        return multiStateView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VdpApplication.getRefWatcher(getContext()).watch(this);
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
