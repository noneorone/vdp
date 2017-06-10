package com.dmio.org.tut.activity.list;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dmio.org.tut.R;
import com.dmio.org.tut.data.model.Warrior;
import com.dmio.org.tut.utils.DeviceUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noo.core.log.Logger;
import com.noo.core.task.AsynTask;
import com.noo.core.ui.VdpActivity;
import com.noo.core.ui.msv.ViewType;
import com.noo.core.utils.AssertUtils;
import com.noo.core.utils.ViewUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明：带刷新样式的RecyclerView<br/>
 * 作者：wangmeng on 2017/3/29 19:56<br/>
 * 邮箱：noneorone@yeah.net
 */
public class ListRecyclerActivity extends VdpActivity {

    private static final String TASK_GET_LIST = "_task_get_list";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout cmpRefresh;

    private List<Warrior> data;
    private ListRecyclerAdapter mAdapter;

    private boolean isLoading;
    private int lastVisibleItemPosition;

    private Handler handler;

    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recycler_view);
        initView();
        setTitle("ListRecycler");
//        int i = 0;
//        int b = 2 / i;
        showView(ViewType.LOADING);
        AsynTask.getInstance().exec(TASK_GET_LIST, ListRecyclerActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("trace");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DeviceUtils.trackANRTrace(ListRecyclerActivity.this);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {
        data = new ArrayList<>();
        mAdapter = new ListRecyclerAdapter(this, data);
        mRecyclerView = ViewUtils.get(this, R.id.rv_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new VerticalLineDecorator(2));
        mRecyclerView.setAdapter(mAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Logger.d("StateChanged = " + newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == mAdapter.getItemCount()) {
                    Logger.d("loading executed");
                    boolean isRefreshing = cmpRefresh.isRefreshing();
                    if (isRefreshing) {
                        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AsynTask.getInstance().exec(TASK_GET_LIST, ListRecyclerActivity.this);
                            }
                        }, 1000L);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Logger.d("onScrolled");
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            }
        });

        cmpRefresh = ViewUtils.get(this, R.id.cmp_refresh);
        cmpRefresh.setColorSchemeResources(R.color.c1);
        cmpRefresh.setDrawingCacheBackgroundColor(R.color.c9);
        cmpRefresh.setProgressBackgroundColorSchemeResource(R.color.c8);
//        cmpRefresh.post(new Runnable() {
//            @Override
//            public void run() {
//                cmpRefresh.setRefreshing(true);
//                AsynTask.getInstance().exec(TASK_GET_LIST, ListRecyclerActivity.this);
//            }
//        });
        cmpRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                AsynTask.getInstance().exec(TASK_GET_LIST, ListRecyclerActivity.this);
            }
        });
    }

    @Override
    public Object onBackgroundProcess(String tag, Object... params) {
        String jsonData = AssertUtils.getJsonData(this, "list_recycler.json");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Warrior>>() {
        }.getType();
        List<Warrior> list = gson.fromJson(jsonData, type);
        return list;
    }

    @Override
    public void onResultSuccess(String tag, Object object) {
        List<Warrior> list = (List<Warrior>) object;
        if (list != null && !list.isEmpty()) {
            data.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
        isLoading = false;
        cmpRefresh.setRefreshing(false);
        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
        showView(ViewType.CONTENT);
    }

    /**
     * Custom item decoration
     */
    public class VerticalLineDecorator extends RecyclerView.ItemDecoration {
        private int space = 0;

        public VerticalLineDecorator(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = space;

            outRect.bottom = space;
        }
    }

}
