package com.lcp.arecyclerview.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.widget.Toast;

import com.lcp.adapter.SpaceDecoration;
import com.lcp.arecyclerview.ALoadMoreAdapterWrapper;
import com.lcp.arecyclerview.ARecyclerViewHelper;
import com.lcp.arecyclerview.adapter.GridMoreAdapter;

import java.util.ArrayList;

public class GridMoreActivity extends BaseActivity {


    private static Handler mHandler = new Handler();
    private ALoadMoreAdapterWrapper aLoadMoreAdapterWrapper;
    private GridMoreAdapter gridMoreAdapter;
    private ARecyclerViewHelper aRecyclerViewHelper;

    @Override
    protected void init() {
        initData();
    }

    private void initData() {
        gridMoreAdapter = new GridMoreAdapter(mDatas);
        aLoadMoreAdapterWrapper = new ALoadMoreAdapterWrapper(gridMoreAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));//在setAdapter之前setLayoutManager
        recyclerView.setAdapter(aLoadMoreAdapterWrapper);
        recyclerView.addItemDecoration(new SpaceDecoration(10));
        aRecyclerViewHelper = new ARecyclerViewHelper(recyclerView, aLoadMoreAdapterWrapper);

        aRecyclerViewHelper.setOnLoadMoreListener(new ARecyclerViewHelper.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long l = System.currentTimeMillis();
                        mDatas.add(0, "下拉刷新的数据" + l);
                        aLoadMoreAdapterWrapper.notifyItemInserted(0);
                        recyclerView.scrollToPosition(0);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });
    }

    boolean loadError = true;

    private void loadMoreData() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loadError) {
                    loadError = false;
                    Toast.makeText(getApplicationContext(), "load more data error", Toast.LENGTH_SHORT).show();
                } else {
                    int size = mDatas.size();
                    for (int i = 0; i < 5; i++) {
                        mDatas.add("加载更多的数据" + i);
                    }
                    aLoadMoreAdapterWrapper.notifyItemInserted(size);
                }
                aRecyclerViewHelper.setLoadMoreComplete(mDatas.size() > 35);
            }
        }, 1200);
    }

}
