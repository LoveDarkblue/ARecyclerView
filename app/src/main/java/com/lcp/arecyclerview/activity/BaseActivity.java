package com.lcp.arecyclerview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.lcp.arecyclerview.R;

import java.util.ArrayList;

/**
 * Created by Aislli on 2018/8/30 0030.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ArrayList<String> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        mDatas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mDatas.add("index:" + i);
        }
        init();
    }

    protected int getLayoutId(){
        return R.layout.activity_list_more;
    }

    protected abstract void init();
}
