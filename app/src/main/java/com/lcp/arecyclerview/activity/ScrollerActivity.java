package com.lcp.arecyclerview.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lcp.adapter.BaseAdapter;
import com.lcp.adapter.OnLoadMoreListener;
import com.lcp.arecyclerview.widget.MyRecyclerView;
import com.lcp.arecyclerview.R;
import com.lcp.arecyclerview.widget.ScrollerLayout;
import com.lcp.arecyclerview.adapter.ListSwipeItemAdapter;

import java.util.ArrayList;

public class ScrollerActivity extends AppCompatActivity implements View.OnClickListener {

    private ScrollerLayout scrollView;
    protected MyRecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ArrayList<String> mDatas;
    private ListSwipeItemAdapter listSwipeItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);
        scrollView = findViewById(R.id.scrollView);
        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.settop).setOnClickListener(this);
        findViewById(R.id.collect).setOnClickListener(this);
        findViewById(R.id.btn).setOnClickListener(this);

        /*scrollView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(), "item long click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        scrollView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "item click", Toast.LENGTH_SHORT).show();
            }
        });*/

        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);

        initData();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            mDatas.add("index:" + i);
        }

        listSwipeItemAdapter = new ListSwipeItemAdapter(mDatas);
        listSwipeItemAdapter.addHeaderView(getHeaderView());
        listSwipeItemAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                loadMore();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listSwipeItemAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        listSwipeItemAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "item click:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        listSwipeItemAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(getApplicationContext(), "item long click:" + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "click " + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
    }

    public void open(View view) {
        scrollView.open();
    }

    public void close(View view) {
        scrollView.close();
    }

    boolean loadError = true;
    private static Handler mHandler = new Handler();
    private void loadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loadError) {
                    loadError = false;
                    listSwipeItemAdapter.loadFailed();
                    return;
                }
                long l = System.currentTimeMillis();
                listSwipeItemAdapter.addLoadMoreData("加载更多的数据" + l);
                listSwipeItemAdapter.addLoadMoreData("加载更多的数据" + l);
                listSwipeItemAdapter.addLoadMoreData("加载更多的数据" + l);

                if (mDatas.size() > 23) {
                    listSwipeItemAdapter.loadEnd();
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
