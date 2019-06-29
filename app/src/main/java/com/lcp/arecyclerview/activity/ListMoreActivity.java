package com.lcp.arecyclerview.activity;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lcp.arecyclerview.ALoadMoreAdapterWrapper;
import com.lcp.arecyclerview.ARecyclerViewHelper;
import com.lcp.arecyclerview.adapter.ListMoreAdapter;
import com.lcp.arecyclerview.R;

import java.util.ArrayList;

public class ListMoreActivity extends BaseActivity {


    private static Handler mHandler = new Handler();
    private ALoadMoreAdapterWrapper aLoadMoreAdapterWrapper;
    boolean loadError = true;

    @Override
    protected void init() {
        initData();
    }

    private void initData() {
        ListMoreAdapter listMoreAdapter = new ListMoreAdapter(mDatas);
        aLoadMoreAdapterWrapper = new ALoadMoreAdapterWrapper(listMoreAdapter);//装饰Adapter
        recyclerView.setAdapter(aLoadMoreAdapterWrapper);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        final ARecyclerViewHelper aRecyclerViewHelper = new ARecyclerViewHelper(recyclerView, aLoadMoreAdapterWrapper);//工具类

        aRecyclerViewHelper.setOnLoadMoreListener(new ARecyclerViewHelper.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (loadError) {
                            loadError = false;
                            Toast.makeText(getApplicationContext(), "load more data error", Toast.LENGTH_SHORT).show();
                        } else {
                            long l = System.currentTimeMillis();
                            mDatas.add("加载更多的数据" + l);
                            aLoadMoreAdapterWrapper.notifyItemInserted(mDatas.size());
                            mDatas.add("加载更多的数据" + l);
                            aLoadMoreAdapterWrapper.notifyItemInserted(mDatas.size());
                        }
                        aRecyclerViewHelper.setLoadMoreComplete(mDatas.size() > 23);
                    }
                }, 1200);
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

}
