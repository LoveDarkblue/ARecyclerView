package com.lcp.arecyclerview.adapter;

import com.lcp.adapter.BaseAdapter;
import com.lcp.adapter.BaseViewHolder;
import com.lcp.arecyclerview.R;

import java.util.List;

/**
 * Created by Aislli on 2018/9/4 0004.
 */
public class ListMore2Adapter extends BaseAdapter<String> {
    public ListMore2Adapter(List<String> mDatas) {
        super(mDatas);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_list;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        viewHolder.setText(R.id.text, item);
    }
}
