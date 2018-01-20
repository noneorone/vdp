package com.noo.app.activity.main;

import com.noo.app.activity.demo.view.paint.PaintActivity;
import com.noo.app.activity.demo.webview.WebViewTestActivity;
import com.noo.app.activity.demo.widget.drag.DragActivity;
import com.noo.app.activity.demo.widget.qrcode.DecoderActivity;
import com.noo.app.activity.list.ListRecyclerActivity;
import com.noo.core.ui.web.VdpWebViewAct;

/**
 * 组件配置
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/26 9:43<br/>
 * @since 1.0
 */
public class Component {

    private Component() {
    }

    /**
     * 组件类
     */
    public static final Class[] ENTRANCE = {
            VdpWebViewAct.class,
            WebViewTestActivity.class,
            PaintActivity.class,
            DecoderActivity.class,
            ListRecyclerActivity.class,
            DragActivity.class
    };

}
