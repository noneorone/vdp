package com.dmio.org.tut.activity.list;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dmio.org.tut.R;
import com.dmio.org.tut.data.model.Warrior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noo.core.log.Logger;
import com.noo.core.task.Task;
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
public class ListRecyclerActivity extends AppCompatActivity implements Task.CallBack<List<Warrior>> {

    private Toolbar mToolBar;

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
    }

    private void initView() {
        mToolBar = ViewUtils.get(this, R.id.toolbar);
        mToolBar.setTitle("RecyclerView Refresh List");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
                                Task.getInstance().exec(ListRecyclerActivity.this);
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
        cmpRefresh.post(new Runnable() {
            @Override
            public void run() {
                cmpRefresh.setRefreshing(true);
                Task.getInstance().exec(ListRecyclerActivity.this);
            }
        });
        cmpRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                Task.getInstance().exec(ListRecyclerActivity.this);
            }
        });
    }

    @Override
    public void preExec() {
    }

    @Override
    public List<Warrior> inHandle() {
        String jsonData = AssertUtils.getJsonData(this, "list_recycler.json");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Warrior>>() {
        }.getType();
        List<Warrior> list = gson.fromJson(jsonData, type);
        return list;
    }

    @Override
    public void complete(final List<Warrior> list) {
        if (list != null && !list.isEmpty()) {
            data.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
        isLoading = false;
        cmpRefresh.setRefreshing(false);
        mAdapter.notifyItemRemoved(mAdapter.getItemCount());
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
