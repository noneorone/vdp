package com.dmio.org.tut.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dmio.org.tut.R;
import com.dmio.org.tut.activity.demo.view.image.WaveViewActivity;
import com.dmio.org.tut.activity.demo.view.paint.PaintActivity;
import com.dmio.org.tut.activity.demo.widget.marqueeview.MarqueeViewActivity;
import com.dmio.org.tut.activity.demo.widget.qrcode.DecoderActivity;
import com.dmio.org.tut.activity.demo.widget.sweetalert.SweetAlertActivity;
import com.dmio.org.tut.activity.demo.widget.wheelpicker.WheelPickerActivity;
import com.dmio.org.tut.activity.demo.security.ca.SecurityCAActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {

    private ComponentAdapter mComponentAdapter;

    @BindView(R.id.lv_list)
    ListView mLvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private List<Map.Entry<String, Class<?>>> getData() {
        final HashMap<String, Class<?>> map = new HashMap<>();
        map.put("Paint", PaintActivity.class);
        map.put("WheelPicker", WheelPickerActivity.class);
        map.put("SweetAlert", SweetAlertActivity.class);
        map.put("Decoder", DecoderActivity.class);
        map.put("MarqueeView", MarqueeViewActivity.class);
        map.put("WaveView", WaveViewActivity.class);
        map.put("SecurityCA", SecurityCAActivity.class);


        final List<Map.Entry<String, Class<?>>> data = new ArrayList<>();
        Set<Map.Entry<String, Class<?>>> entries = map.entrySet();
        for (Iterator<Map.Entry<String, Class<?>>> iterator = entries.iterator(); iterator.hasNext(); ) {
            data.add(iterator.next());
        }

        return data;
    }

    private void initView() {
        mComponentAdapter = new ComponentAdapter(getData());
        mLvList.setAdapter(mComponentAdapter);
    }

    @OnItemClick({R.id.lv_list})
    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        Map.Entry<String, Class<?>> entry = (Map.Entry<String, Class<?>>) parent.getAdapter().getItem(position);
        if (entry != null) {
            startActivity(new Intent(this, entry.getValue()));
        }
    }

    private class ComponentAdapter extends BaseAdapter {

        private List<Map.Entry<String, Class<?>>> data;

        public ComponentAdapter(List<Map.Entry<String, Class<?>>> data) {
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

            Map.Entry<String, Class<?>> entry = data.get(position);
            if (entry != null) {
                holder.tvTitle.setText(entry.getKey());
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
