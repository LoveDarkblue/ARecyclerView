package com.lcp.arecyclerview.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import com.lcp.adapter.BaseAdapter;
import com.lcp.adapter.BaseViewHolder;
import com.lcp.adapter.MyItemTouchHelperCallBack;
import com.lcp.arecyclerview.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Aislli on 2019/4/5 0008.
 */
public class ItemTouchHelperAdapter extends BaseAdapter<String> implements MyItemTouchHelperCallBack.OnItemTouchHelperCallBack {
    public ItemTouchHelperAdapter(List<String> mDatas) {
        super(mDatas);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_list;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        viewHolder.itemView.setBackgroundColor(Color.parseColor("#DEC2C2"));
        viewHolder.setText(R.id.text, item);
    }

    @Override
    public boolean onMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int adapterPosition = viewHolder.getAdapterPosition();
        int adapterPosition1 = getHeaderViewCount() > 0 ? target.getAdapterPosition() - 1 : target.getAdapterPosition();
        if (adapterPosition1 <= 2) {//例如设置position小于2的item不让换
            return false;
        }
        Collections.swap(mDatas, adapterPosition, adapterPosition1);
        notifyItemMoved(adapterPosition, adapterPosition1);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        int adapterPosition = viewHolder.getAdapterPosition();
        notifyItemRemoved(adapterPosition);
        mDatas.remove(getHeaderViewCount() > 0 ? adapterPosition - 1 : adapterPosition);
        //为了防止删到不满一屏时加载更多的item出现
        hiddenFooterView();
    }
}