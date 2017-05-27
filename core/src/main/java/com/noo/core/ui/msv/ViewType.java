package com.noo.core.ui.msv;


/**
 * 视图类型，根据{@link com.noo.core.ui.msv.MultiStateView.ViewState}关联
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/15 16:35<br/>
 * @since 1.0
 */
public enum ViewType {
    UNKNOWN(MultiStateView.VIEW_STATE_UNKNOWN),
    CONTENT(MultiStateView.VIEW_STATE_CONTENT),
    ERROR(MultiStateView.VIEW_STATE_ERROR),
    EMPTY(MultiStateView.VIEW_STATE_EMPTY),
    LOADING(MultiStateView.VIEW_STATE_LOADING);

    private int value;

    public int getValue() {
        return value;
    }

    ViewType(int value) {
        this.value = value;
    }
}

