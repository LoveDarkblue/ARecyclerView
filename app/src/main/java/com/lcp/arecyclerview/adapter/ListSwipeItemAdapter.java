package com.lcp.arecyclerview.adapter;

import android.view.View;
import android.widget.Toast;

import com.lcp.adapter.BaseAdapter;
import com.lcp.adapter.BaseViewHolder;
import com.lcp.arecyclerview.R;

import java.util.List;

/**
 * Created by Aislli on 2018/11/6 0004.
 */
public class ListSwipeItemAdapter extends BaseAdapter<String> {
    public ListSwipeItemAdapter(List<String> mDatas) {
        super(mDatas);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_swipe;
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, String item) {
        viewHolder.setText(R.id.text, item);
        if (viewHolder.getAdapterPosition() % 2 == 0) {
            viewHolder.getView(R.id.collect).setVisibility(View.GONE);
        }else {
            viewHolder.getView(R.id.collect).setVisibility(View.VISIBLE);
        }
        viewHolder.getView(R.id.settop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "top "+viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.getView(R.id.collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "collect "+viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "delete "+viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
