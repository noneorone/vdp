package com.noo.core.ui.msv;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.noo.core.ui.msv.MultiStateView.ViewState;

/**
 * MultiStateView处理类
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/16 16:35<br/>
 * @since 1.0
 */

public class MultiStateViewHelper {

    private MultiStateViewHelper() {
    }

    public static MultiStateView attachView(@NonNull View contentView, @LayoutRes int loadingLayoutId, @LayoutRes int errorLayoutId, @LayoutRes int emptyLayoutId, @ViewState int viewState, boolean animateChange) {
        Context context = contentView.getContext();
        MultiStateView multiStateView = new MultiStateView(context);
        setAttrs(multiStateView, contentView, loadingLayoutId, errorLayoutId, emptyLayoutId, viewState, animateChange);
        return multiStateView;
    }

    public static MultiStateView attachView(@NonNull View contentView, View loadingView, View errorView, View emptyView, @ViewState int viewState, boolean animateChange) {
        Context context = contentView.getContext();
        MultiStateView multiStateView = new MultiStateView(context);
        setAttrs(multiStateView, contentView, loadingView, errorView, emptyView, viewState, animateChange);
        return multiStateView;
    }

    public static void setAttrs(MultiStateView multiStateView, @NonNull View contentView, Object loadingLayout, Object errorLayout, Object emptyLayout, @ViewState int viewState, boolean animateChange) {
        int vaildViewState = MultiStateView.VIEW_STATE_CONTENT;
        multiStateView.setAnimateLayoutChanges(animateChange);
        multiStateView.setViewForState(contentView, MultiStateView.VIEW_STATE_CONTENT);
        if (loadingLayout != null) {
            if (viewState == MultiStateView.VIEW_STATE_LOADING) {
                vaildViewState = MultiStateView.VIEW_STATE_LOADING;
            }
            if (loadingLayout instanceof Integer) {
                int loadingLayoutId = (int) loadingLayout;
                if (loadingLayoutId > -1) {
                    multiStateView.setViewForState(loadingLayoutId, MultiStateView.VIEW_STATE_LOADING);
                }
            } else if (loadingLayout instanceof View) {
                View loadingView = (View) loadingLayout;
                if (loadingView != null) {
                    multiStateView.setViewForState(loadingView, MultiStateView.VIEW_STATE_LOADING);
                }
            }
        }
        if (errorLayout != null) {
            if (viewState == MultiStateView.VIEW_STATE_ERROR) {
                vaildViewState = MultiStateView.VIEW_STATE_ERROR;
            }
            if (errorLayout instanceof Integer) {
                int errorLayoutId = (int) errorLayout;
                if (errorLayoutId > -1) {
                    multiStateView.setViewForState(errorLayoutId, MultiStateView.VIEW_STATE_ERROR);
                }
            } else if (errorLayout instanceof View) {
                View errorView = (View) errorLayout;
                if (errorView != null) {
                    multiStateView.setViewForState(errorView, MultiStateView.VIEW_STATE_ERROR);
                }
            }
        }
        if (emptyLayout != null) {
            if (viewState == MultiStateView.VIEW_STATE_EMPTY) {
                vaildViewState = MultiStateView.VIEW_STATE_EMPTY;
            }
            if (emptyLayout instanceof Integer) {
                int emptyLayoutId = (int) emptyLayout;
                if (emptyLayoutId > -1) {
                    multiStateView.setViewForState(emptyLayoutId, MultiStateView.VIEW_STATE_EMPTY);
                }
            } else if (emptyLayout instanceof View) {
                View emptyView = (View) emptyLayout;
                if (emptyView != null) {
                    multiStateView.setViewForState(emptyView, MultiStateView.VIEW_STATE_EMPTY);
                }
            }
        }
        multiStateView.setViewState(vaildViewState);
    }

}
