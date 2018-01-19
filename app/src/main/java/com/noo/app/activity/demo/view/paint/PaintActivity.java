package com.noo.app.activity.demo.view.paint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.noo.app.R;
import com.noo.app.widget.DoughnutView;

public class PaintActivity extends AppCompatActivity {

    private DoughnutView mDvQuotaPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_paint_main);
        initViews();
    }

    private void initViews() {
        mDvQuotaPercent = (DoughnutView) findViewById(R.id.dv_quota_percent);
        mDvQuotaPercent.setValue(0.56f * 360);
    }


}
