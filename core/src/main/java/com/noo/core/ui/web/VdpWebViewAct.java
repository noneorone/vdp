package com.noo.core.ui.web;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.noo.core.R;
import com.noo.core.ui.VdpActivity;
import com.noo.core.utils.ComponentUtils;

/**
 * WebView组件封装{@link VdpActivity}
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/25 15:39<br/>
 * @since 1.0
 */
public class VdpWebViewAct extends VdpActivity {

    private VdpWebViewFrm webViewFrm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vdp_empty_frm);
        initView();
    }

    private void initView() {
        Bundle extras = new Bundle();
        extras.putString(VdpWebViewFrm.EXTRA_URL, "http://www.baidu.com");
        webViewFrm = new VdpWebViewFrm();
        ComponentUtils.initFrament(webViewFrm, this, R.id.vdp_content_area, extras);
    }

    @Override
    public void onBackPressed() {
        if (webViewFrm == null || !webViewFrm.goBack()) {
            super.onBackPressed();
        }
    }
}
