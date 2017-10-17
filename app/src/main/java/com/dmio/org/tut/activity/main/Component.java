package com.dmio.org.tut.activity.main;

import com.dmio.org.tut.activity.demo.moxie.MoxieActivity;
import com.dmio.org.tut.activity.demo.security.ca.SecurityCAActivity;
import com.dmio.org.tut.activity.demo.view.image.WaveViewActivity;
import com.dmio.org.tut.activity.demo.view.paint.PaintActivity;
import com.dmio.org.tut.activity.demo.webview.WebViewTestActivity;
import com.dmio.org.tut.activity.demo.widget.blurry.BlurryActivity;
import com.dmio.org.tut.activity.demo.widget.drag.DragActivity;
import com.dmio.org.tut.activity.demo.widget.marqueeview.MarqueeViewActivity;
import com.dmio.org.tut.activity.demo.widget.progress.ProgressWheelActivity;
import com.dmio.org.tut.activity.demo.widget.qrcode.DecoderActivity;
import com.dmio.org.tut.activity.demo.widget.sweetalert.SweetAlertActivity;
import com.dmio.org.tut.activity.demo.widget.swipecard.SwipeCardActivity;
import com.dmio.org.tut.activity.demo.widget.wheelpicker.WheelPickerActivity;
import com.dmio.org.tut.activity.guide.GuideMainActivity;
import com.dmio.org.tut.activity.list.ListRecyclerActivity;
import com.dmio.org.tut.activity.demo.widget.tourguide.MultipleToolTipActivity;
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
            PaintActivity.class,
            WheelPickerActivity.class,
            SweetAlertActivity.class,
            DecoderActivity.class,
            MarqueeViewActivity.class,
            WaveViewActivity.class,
            SecurityCAActivity.class,
            GuideMainActivity.class,
            SwipeCardActivity.class,
            BlurryActivity.class,
            MoxieActivity.class,
            ListRecyclerActivity.class,
            ProgressWheelActivity.class,
            DragActivity.class,
            VdpWebViewAct.class,
            MultipleToolTipActivity.class,
            WebViewTestActivity.class
    };

}
