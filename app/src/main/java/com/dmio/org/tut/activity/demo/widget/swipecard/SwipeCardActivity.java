package com.dmio.org.tut.activity.demo.widget.swipecard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;


import com.dmio.org.tut.R;
import com.dmio.org.tut.widget.swipecard.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SwipeCardActivity extends Activity {

    private ArrayList<CardMode> al;
    private CardAdapter adapter;
    private int i;
    private SwipeFlingAdapterView flingContainer;
    private List<List<String>> list = new ArrayList<>();
    private ImageView left, right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card);
        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);
        left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                left();
            }
        });
        right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                right();
            }
        });
        al = new ArrayList<>();

        for (int i = 0; i < Config.IMAGE_URLS.length; i++) {
            List<String> s = new ArrayList<>();
            s.add(Config.IMAGE_URLS[i]);
            list.add(s);
        }
        List<String> yi;
        al.add(new CardMode("测试00", 21, list.get(0)));
        al.add(new CardMode("测试11", 21, list.get(1)));
        al.add(new CardMode("测试22", 18, list.get(2)));
        al.add(new CardMode("测试1", 21, list.get(3)));
        al.add(new CardMode("测试2", 21, list.get(4)));
        al.add(new CardMode("测试3", 21, list.get(5)));
        al.add(new CardMode("测试4", 21, list.get(6)));
        al.add(new CardMode("测试5", 21, list.get(7)));
        al.add(new CardMode("测试6", 21, list.get(8)));
        al.add(new CardMode("测试7", 21, list.get(9)));
        al.add(new CardMode("测试8", 21, list.get(10)));
        al.add(new CardMode("测试9", 21, list.get(11)));
        al.add(new CardMode("测试10", 21, list.get(12)));
        al.add(new CardMode("测试11", 21, list.get(13)));
        al.add(new CardMode("测试12", 21, list.get(14)));

        adapter = new CardAdapter(this, al);
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                al.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                makeToast(SwipeCardActivity.this, "不喜欢");
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                makeToast(SwipeCardActivity.this, "喜欢");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                al.add(new CardMode("循环测试", 18, Arrays.asList(new String[]{Config.IMAGE_URLS[new Random().nextInt(Config.IMAGE_URLS.length - 1)]})));
                adapter.notifyDataSetChanged();
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                try {
                    Log.d(getClass().getName(), "scrollProgressPercent: " + scrollProgressPercent);
                    View view = flingContainer.getSelectedView();
                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                    view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(SwipeCardActivity.this, "点击图片");
            }
        });

    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }


    public void right() {
        flingContainer.getTopCardListener().selectRight();
    }

    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }


}
