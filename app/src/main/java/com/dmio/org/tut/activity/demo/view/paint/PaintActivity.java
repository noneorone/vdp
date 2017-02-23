package com.dmio.org.tut.activity.demo.view.paint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dmio.org.tut.R;
import com.dmio.org.tut.widget.DoughnutView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaintActivity extends AppCompatActivity {

    @BindView(R.id.dv_quota_percent)
    DoughnutView mDvQuotaPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_paint_main);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        mDvQuotaPercent.setValue(0.56f * 360);
    }


}
