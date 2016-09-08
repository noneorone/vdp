package com.dmio.org.tut.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dmio.org.tut.R;
import com.dmio.org.tut.activity.demo.paint.PaintActivity;
import com.dmio.org.tut.activity.demo.widget.qrcode.DecoderActivity;
import com.dmio.org.tut.activity.demo.widget.sweetalert.SweetAlertActivity;
import com.dmio.org.tut.activity.demo.widget.wheelpicker.WheelPickerActivity;

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


        final List<Map.Entry<String, Class<?>>> data = new ArrayList<>();
        Set<Map.Entry<String, Class<?>>> entries = map.entrySet();
        for (Iterator<Map.Entry<String, Class<?>>> iterator = entries.iterator(); iterator.hasNext();) {
            data.add(iterator.next());
        }

        return data;
    }

    private void initView() {
        final List<Map.Entry<String, Class<?>>> data = getData();

        mLvList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                ViewHolder holder = null;
                if (view == null) {
                    view = getLayoutInflater().inflate(R.layout.list_main_item, null);
                    holder = new ViewHolder(view);
                    view.setTag(holder);
                } else {
                    holder = (ViewHolder) view.getTag();
                }

                Map.Entry<String, Class<?>> entry = data.get(i);
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

        });
    }

    @OnItemClick({R.id.lv_list})
    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
        Map.Entry<String, Class<?>> entry = getData().get(position);
        if (entry != null) {
            startActivity(new Intent(this, entry.getValue()));
        }
    }


}
