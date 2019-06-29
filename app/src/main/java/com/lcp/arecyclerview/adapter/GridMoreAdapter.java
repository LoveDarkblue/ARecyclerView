package com.lcp.arecyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcp.arecyclerview.R;

import java.util.List;

/**
 * Created by Aislli on 2018/8/20 0020.
 */
public class GridMoreAdapter extends RecyclerView.Adapter<GridMoreAdapter.MyViewHolder> {
    private List<String> mList;

    public GridMoreAdapter(List<String> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;

        private MyViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.text);
        }
    }
}
