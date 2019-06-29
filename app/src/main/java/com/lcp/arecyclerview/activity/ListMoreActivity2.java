package com.lcp.arecyclerview.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.lcp.adapter.BaseAdapter;
import com.lcp.adapter.OnLoadMoreListener;
import com.lcp.arecyclerview.adapter.ListMore2Adapter;

public class ListMoreActivity2 extends BaseActivity {


    private static Handler mHandler = new Handler();
    private ListMore2Adapter listMoreAdapter;

    @Override
    protected void init() {
        initData();
    }

    private void initData() {
        listMoreAdapter = new ListMore2Adapter(mDatas);
        listMoreAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                loadMore();
            }
        });
        //要在setAdapter之前设置OnLoadMoreListener,因为要通过设置Listener来标记加载更多功能开启，如果不这样做，就得在Adapter的构造函数里把标记传过去
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listMoreAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long l = System.currentTimeMillis();
                        for (int i = 0; i < 3; i++) {
                            listMoreAdapter.addRefreshData("刷新的数据" + l);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });

        listMoreAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(),"item:"+position,Toast.LENGTH_SHORT).show();
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
                long l = System.currentTimeMillis();
                listMoreAdapter.addLoadMoreData("加载更多的数据" + l);
                listMoreAdapter.addLoadMoreData("加载更多的数据" + l);

                if (mDatas.size() > 23) {
                    listMoreAdapter.loadEnd();
                }
                /*if (Math.random() * 10 > 3) {
                    long l = System.currentTimeMillis();
                    listMoreAdapter.addLoadMoreData("加载更多的数据" + l);
                    listMoreAdapter.addLoadMoreData("加载更多的数据" + l);
                } else {
                    listMoreAdapter.loadFailed();
                }*/
            }
        }, 1200);
    }

}
