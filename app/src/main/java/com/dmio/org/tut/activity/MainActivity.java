package com.dmio.org.tut.activity;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dmio.org.tut.R;
import com.dmio.org.tut.activity.demo.moxie.MoxieActivity;
import com.dmio.org.tut.activity.demo.security.ca.SecurityCAActivity;
import com.dmio.org.tut.activity.demo.view.image.WaveViewActivity;
import com.dmio.org.tut.activity.demo.view.paint.PaintActivity;
import com.dmio.org.tut.activity.demo.widget.blurry.BlurryActivity;
import com.dmio.org.tut.activity.demo.widget.marqueeview.MarqueeViewActivity;
import com.dmio.org.tut.activity.demo.widget.qrcode.DecoderActivity;
import com.dmio.org.tut.activity.demo.widget.sweetalert.SweetAlertActivity;
import com.dmio.org.tut.activity.demo.widget.swipecard.SwipeCardActivity;
import com.dmio.org.tut.activity.demo.widget.wheelpicker.WheelPickerActivity;
import com.dmio.org.tut.activity.guide.GuideMainActivity;
import com.dmio.org.tut.activity.list.ListRecyclerActivity;
import com.dmio.org.tut.core.log.Logger;
import com.dmio.org.tut.core.utils.AppUtils;
import com.dmio.org.tut.utils.DeviceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    private final int REQ_CODE_PERM_WES = 0x001;

    private ComponentAdapter mComponentAdapter;

    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.lv_list)
    ListView mLvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_CODE_PERM_WES);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_PERM_WES:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Logger.d("twlis: " + AppUtils.getMetaData(this, Application.class, "twcal"));
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("trace");
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DeviceUtils.trackANRTrace(MainActivity.this);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mComponentAdapter = new ComponentAdapter();
        mLvList.setAdapter(mComponentAdapter);
    }

    @OnItemClick({R.id.lv_list})
    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        try {
            Class clz = (Class) parent.getAdapter().getItem(position);
            if (clz != null) {
                ActivityCompat.startActivity(this, new Intent(getApplicationContext(), clz), null);
            }
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    private class ComponentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return COMPONENT_CLASSES.length;
        }

        @Override
        public Class getItem(int position) {
            return COMPONENT_CLASSES[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.list_main_item, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            Class clz = getItem(position);
            if (clz != null) {
                String entry = clz.getSimpleName();
                int index = entry.toLowerCase().lastIndexOf("activity");
                if (index != -1) {
                    entry = entry.substring(0, index);
                }
                holder.tvTitle.setText(entry);
            }

            return view;
        }


        class ViewHolder {
            private final TextView tvTitle;

            ViewHolder(View root) {
                tvTitle = (TextView) root.findViewById(R.id.tv_title);
            }
        }

    }

    /**
     * 组件类
     */
    private static final Class[] COMPONENT_CLASSES = {
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
    };

}

