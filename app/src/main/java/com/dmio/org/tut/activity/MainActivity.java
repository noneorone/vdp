package com.dmio.org.tut.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dmio.org.tut.R;
import com.dmio.org.tut.utils.AppUtils;
import com.dmio.org.tut.utils.DeviceUtils;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ComponentAdapter mComponentAdapter;

    @BindView(R.id.lv_list)
    ListView mLvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        Log.d(TAG, "twlis: " + AppUtils.getMetaData(this, Application.class, "twcal"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("test");
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
        List<String> data = Arrays.asList(getResources().getStringArray(R.array.Entrance));
        mComponentAdapter = new ComponentAdapter(data);
        mLvList.setAdapter(mComponentAdapter);
    }

    @OnItemClick({R.id.lv_list})
    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        try {
            String entry = (String) parent.getAdapter().getItem(position);
            if (!TextUtils.isEmpty(entry)) {
                startActivity(new Intent(this, Class.forName(entry)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ComponentAdapter extends BaseAdapter {

        private List<String> data;

        public ComponentAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            if (data != null) {
                return data.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (data != null) {
                return data.get(position);
            }
            return null;
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

            String entry = data.get(position);
            if (!TextUtils.isEmpty(entry)) {
                entry = entry.substring(entry.lastIndexOf(".") + 1);
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


}

