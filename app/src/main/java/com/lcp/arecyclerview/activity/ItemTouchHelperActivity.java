package com.lcp.arecyclerview.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lcp.adapter.BaseAdapter;
import com.lcp.adapter.MyItemTouchHelperCallBack;
import com.lcp.adapter.OnLoadMoreListener;
import com.lcp.arecyclerview.R;
import com.lcp.arecyclerview.adapter.ItemTouchHelperAdapter;

public class ItemTouchHelperActivity extends BaseActivity {
    private static Handler mHandler = new Handler();
    private ItemTouchHelperAdapter itemTouchHelperAdapter;

    @Override
    protected void init() {
        itemTouchHelperAdapter = new ItemTouchHelperAdapter(mDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemTouchHelperAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                loadMore();
            }
        });
        itemTouchHelperAdapter.addHeaderView(getHeaderView());
        recyclerView.setAdapter(itemTouchHelperAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallBack(itemTouchHelperAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        itemTouchHelperAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ItemTouchHelperActivity.this, "position:"+position, Toast.LENGTH_SHORT).show();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long l = System.currentTimeMillis();
                        for (int i = 0; i < 3; i++) {
                            itemTouchHelperAdapter.addRefreshData("刷新的数据" + l);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });
    }

    private void loadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long l = System.currentTimeMillis();
                itemTouchHelperAdapter.addLoadMoreData("加载更多的数据" + l);
                itemTouchHelperAdapter.addLoadMoreData("加载更多的数据" + l);

                if (mDatas.size() > 35) {
                    itemTouchHelperAdapter.loadEnd();
                }
            }
        }, 1200);
    }

    private View getHeaderView() {
        View view =  getLayoutInflater().inflate(R.layout.item_header, (ViewGroup) recyclerView.getParent(), false);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        marginLayoutParams.bottomMargin = dip2px(this,1);
        view.setLayoutParams(marginLayoutParams);
        return view;
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
