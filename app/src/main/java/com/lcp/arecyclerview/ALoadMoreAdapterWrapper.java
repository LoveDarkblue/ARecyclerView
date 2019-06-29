package com.lcp.arecyclerview;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 加载更多数据逻辑的处理
 * Created by Aislli on 2018/8/20 0020.
 */
@Deprecated
public class ALoadMoreAdapterWrapper extends RecyclerView.Adapter {
    private static final int ITEM_TYPE_LOADMOTE = 100001;

    private boolean enableLoadMore = true;
    private LoadMoreHolder mLoadMoreHolder;
    private RecyclerView.Adapter mAdapter;

    public ALoadMoreAdapterWrapper(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooterView(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    private boolean isFooterView(int position) {
        return enableLoadMore && position >= getItemCount() - 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOADMOTE) {
            if (null == mLoadMoreHolder) {
                mLoadMoreHolder = new LoadMoreHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, parent, false));
                setLoadMoreVisible(true);
            }
            return mLoadMoreHolder;
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != ITEM_TYPE_LOADMOTE) {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        if (!enableLoadMore) {
            return mAdapter.getItemCount();
        }
        return mAdapter.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (!enableLoadMore) {
            return mAdapter.getItemViewType(position);
        }
        if (position == getItemCount() - 1) {
            return ITEM_TYPE_LOADMOTE;
        }
        return mAdapter.getItemViewType(position);
    }

    public void setLoadMoreEnable(boolean enable) {
        enableLoadMore = enable;
        setLoadMoreVisible(enable);
        notifyItemChanged(getLoadMoreViewPosition());
    }

    private int getLoadMoreViewPosition() {
        return getItemCount();
    }

    public boolean isEnableLoadMore() {
        return enableLoadMore;
    }

    private void setLoadMoreVisible(boolean flag) {
        if (null == mLoadMoreHolder) {
            return;
        }
        mLoadMoreHolder.setLoadMoreViewVisible(flag);
    }

    public void setLoadMoreState(boolean isLoadMore, boolean isFinish) {
        if (isFinish) {
            mLoadMoreHolder.setPbVisible(true);
            mLoadMoreHolder.setTv("-- 到底啦 --");
            mLoadMoreHolder.setBGColor();
            return;
        }
        if (isLoadMore) {
            mLoadMoreHolder.setTv("正在加载...");
        } else {
            mLoadMoreHolder.setTv("上拉加载更多...");
        }
    }

    class LoadMoreHolder extends RecyclerView.ViewHolder {

        private TextView tv;
        private View pb;

        LoadMoreHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.ilm_tv);
            pb = itemView.findViewById(R.id.ilm_pb);
            itemView.setVisibility(View.GONE);
        }

        private void setTv(CharSequence txt) {
            tv.setText(txt);
        }

        private void setPbVisible(boolean isFinish) {
            pb.setVisibility(isFinish ? View.GONE : View.VISIBLE);
        }

        private void setLoadMoreViewVisible(boolean flag) {
            itemView.setVisibility(flag ? View.VISIBLE : View.GONE);
        }

        private void setBGColor() {
            itemView.setBackgroundColor(Color.parseColor("#FFF1EFF0"));
        }
    }
}
