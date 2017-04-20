package com.dmio.org.tut.activity;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.dmio.org.tut.utils.DeviceUtils;
import com.noo.core.log.Logger;
import com.noo.core.ui.BaseActivity;
import com.noo.core.utils.AppUtils;

public class MainActivity extends BaseActivity {

    private final int REQ_CODE_PERM_WES = 0x001;

    private ComponentAdapter mComponentAdapter;

    private RecyclerView mLvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        mLvList = (RecyclerView) getContainer();
        mComponentAdapter = new ComponentAdapter();
        mLvList.setHasFixedSize(true);
        mLvList.setLayoutManager(new LinearLayoutManager(this));
        mLvList.setAdapter(mComponentAdapter);
    }

    @Override
    public int getContentLayouResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onNavClick() {
        super.onNavClick();
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


    private class ComponentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_main_item, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ItemHolder itemHolder = (ItemHolder) holder;
            itemHolder.bindViewData(position);
        }

        @Override
        public int getItemCount() {
            return COMPONENT_CLASSES.length;
        }

        final class ItemHolder extends RecyclerView.ViewHolder {
            private final TextView tvTitle;

            public ItemHolder(View root) {
                super(root);
                tvTitle = (TextView) root.findViewById(R.id.tv_title);
            }

            public void bindViewData(int position) {
                final Class clz = COMPONENT_CLASSES[position];
                String entry = clz.getSimpleName();
                int index = entry.toLowerCase().lastIndexOf("activity");
                if (index != -1) {
                    entry = entry.substring(0, index);
                }
                tvTitle.setText(entry);
                tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.startActivity(MainActivity.this, new Intent(getApplicationContext(), clz), null);
                    }
                });
            }
        }
    }

}

