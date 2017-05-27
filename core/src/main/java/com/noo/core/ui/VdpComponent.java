package com.noo.core.ui;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.noo.core.task.AsynCallBack;
import com.noo.core.ui.msv.MultiStateView;
import com.noo.core.ui.msv.ViewType;

/**
 * VDP结构组件定义
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/17 19:37<br/>
 * @since 1.0
 */
public interface VdpComponent extends AsynCallBack {

    @LayoutRes
    int getLoadingLayoutId();

    @LayoutRes
    int getErrorLayoutId();

    @LayoutRes
    int getEmptyLayoutId();

    MultiStateView getMultiStateView();

    boolean animateViewStateChange();

    void setView(View view, boolean switchToState, ViewType viewType);

    void showView(ViewType viewType);

}
