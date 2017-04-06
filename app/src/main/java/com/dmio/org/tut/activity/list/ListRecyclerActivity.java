package com.dmio.org.tut.activity.list;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dmio.org.tut.R;
import com.dmio.org.tut.core.task.Task;
import com.dmio.org.tut.core.utils.AssertUtils;
import com.dmio.org.tut.core.utils.ViewUtils;
import com.dmio.org.tut.data.model.Warrior;
import com.dmio.org.tut.widget.sweetalert.SweetAlertDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明：带谷歌圆圈刷新样式的RecyclerView<br/>
 * 作者：wangmeng on 2017/3/29 19:56<br/>
 * 邮箱：wangmeng@pycredit.cn
 */
public class ListRecyclerActivity extends AppCompatActivity {


    private RecyclerView mRvList;
    private ListRefreshRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Handler mHandler;

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        return mHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_refresh_recycler_view);
        initView();
        initData();
    }

    private void initView() {
        mRvList = ViewUtils.get(this, R.id.rv_list);

        mRvList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(mLayoutManager);

        List<Warrior> data = new ArrayList<>();
        mAdapter = new ListRefreshRecyclerViewAdapter(this, data);
        mRvList.setAdapter(mAdapter);
    }

    private void initData() {
        final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setContentText("");
        dialog.setTitleText("loading...");

        Task.getInstance().exec(new Task.CallBack<List<Warrior>>() {
            @Override
            public void preExec() {
                if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
            }

            @Override
            public List<Warrior> inHandle() {
                String jsonData = AssertUtils.getJsonData("list_recycler.json");
                Gson gson = new Gson();
                Type type = new TypeToken<List<Warrior>>() {
                }.getType();
                List<Warrior> list = gson.fromJson(jsonData, type);
                return list;
            }

            @Override
            public void complete(final List<Warrior> list) {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (dialog != null) {
                            dialog.dismissWithAnimation();
                        }
                        if (list != null && !list.isEmpty()) {
                            mAdapter.setData(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }, 1000L);
            }
        });
    }

}
