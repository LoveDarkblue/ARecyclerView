package com.lcp.arecyclerview.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcp.arecyclerview.R;
import com.lcp.arecyclerview.adapter.ListMore2Adapter;

public class HeaderActivity extends BaseActivity {


    private static Handler mHandler = new Handler();
    private ListMore2Adapter listMoreAdapter;

    @Override
    protected void init() {
        initData();
    }

    private void initData() {
        listMoreAdapter = new ListMore2Adapter(mDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        View headerView = getHeaderView(0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listMoreAdapter.addHeaderView(getHeaderView(1, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listMoreAdapter.removeHeaderView(v);
                    }
                }),0);
            }
        });
        listMoreAdapter.addHeaderView(headerView);
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
    }

    private View getHeaderView(int type, View.OnClickListener listener) {
        View view =  getLayoutInflater().inflate(R.layout.item_header, (ViewGroup) recyclerView.getParent(), false);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        marginLayoutParams.bottomMargin = dip2px(this,1);
        view.setLayoutParams(marginLayoutParams);
        if (type == 1) {
            TextView tv = view.findViewById(R.id.ih_tv);
            tv.setText("remove");
        }
        view.setOnClickListener(listener);
        return view;
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
