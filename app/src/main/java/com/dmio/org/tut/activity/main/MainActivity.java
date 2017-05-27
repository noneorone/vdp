package com.dmio.org.tut.activity.main;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.dmio.org.tut.R;
import com.dmio.org.tut.utils.DeviceUtils;
import com.noo.core.log.Logger;
import com.noo.core.ui.VdpActivity;
import com.noo.core.ui.splash.VdpSplashView;
import com.noo.core.utils.AppUtils;

public class MainActivity extends VdpActivity {

    private final int REQ_CODE_PERM_WES = 0x001;

    private ComptAdapter mCompAdapter;

    private RecyclerView mLvList;

    @Override
    protected boolean displayHomeAsUpEnabled() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VdpSplashView.show(this, "Skip", R.drawable.ic_splash_light, new VdpSplashView.OnActionListener() {
            @Override
            public void onDismiss() {
//                Snackbar.make(mLvList, "welcome", Snackbar.LENGTH_SHORT).show();
            }
        });
        initView();
        setTitle("Functions Follows");
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
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
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
        mLvList = (RecyclerView) findViewById(R.id.lv_list);
        mCompAdapter = new ComptAdapter(this);
        mLvList.setHasFixedSize(true);
        mLvList.setLayoutManager(new LinearLayoutManager(this));
        mLvList.setAdapter(mCompAdapter);
    }

}

