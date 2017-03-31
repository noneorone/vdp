package com.dmio.org.tut.activity.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dmio.org.tut.R;
import com.dmio.org.tut.data.model.Warrior;
import com.dmio.org.tut.core.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明：带谷歌圆圈刷新样式的RecyclerView<br/>
 * 作者：wangmeng on 2017/3/29 19:56<br/>
 * 邮箱：wangmeng@pycredit.cn
 */
public class ListRefreshRecyclerViewActivity extends AppCompatActivity {


    private RecyclerView mRvList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_refresh_recycler_view);
        initView();
    }

    private void initView() {
        mRvList = ViewUtils.get(this, R.id.rv_list);

        mRvList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLayoutManager);

        List<Warrior> data = new ArrayList<>();
        mAdapter = new ListRefreshRecyclerViewAdapter(data);
        
    }

}
