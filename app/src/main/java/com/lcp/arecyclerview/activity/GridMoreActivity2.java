package com.lcp.arecyclerview.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;

import com.lcp.adapter.OnLoadMoreListener;
import com.lcp.adapter.SpaceDecoration;
import com.lcp.arecyclerview.adapter.GridMore2Adapter;

import java.util.ArrayList;

public class GridMoreActivity2 extends BaseActivity {


    private static Handler mHandler = new Handler();
    private GridMore2Adapter listMoreAdapter;

    @Override
    protected void init() {
        initData();
    }

    private void initData() {
        listMoreAdapter = new GridMore2Adapter(mDatas);

        listMoreAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                loadMore();
            }
        });
        //要在setAdapter之前设置OnLoadMoreListener,因为要通过设置Listener来标记加载更多功能开启
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(listMoreAdapter);
        recyclerView.addItemDecoration(new SpaceDecoration(10));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long l = System.currentTimeMillis();
                        listMoreAdapter.addRefreshData("刷新的数据" + l);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });
    }

    boolean loadError = true;

    private void loadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loadError) {
                    loadError = false;
                    listMoreAdapter.loadFailed();
                    return;
                }
                ArrayList<String> strings = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    strings.add("加载更多的数据" + i);
                }
                listMoreAdapter.addLoadMoreData(strings);
                if (mDatas.size() > 40) {
                    listMoreAdapter.loadEnd();
                }

                /*if (Math.random() * 10 > 3) {
                    long l = System.currentTimeMillis();
                    listMoreAdapter.addLoadMoreData("加载更多的数据" + l);
                    listMoreAdapter.addLoadMoreData("加载更多的数据" + l);
                    listMoreAdapter.addLoadMoreData("加载更多的数据" + l);
                } else {
                    listMoreAdapter.loadFailed();
                }*/
            }
        }, 1200);
    }

}
