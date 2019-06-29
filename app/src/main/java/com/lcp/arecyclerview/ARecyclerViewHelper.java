package com.lcp.arecyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 加载更多UI监听逻辑的处理
 * Created by Aislli on 2018/8/20 0020.
 */
@Deprecated
public class ARecyclerViewHelper extends RecyclerView.OnScrollListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ALoadMoreAdapterWrapper mAdapterWrapper;
    private boolean isLoading;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isFinish;
    private int lastItemCount;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        mAdapterWrapper.setLoadMoreEnable(true);
    }

    public ARecyclerViewHelper(RecyclerView recyclerView, ALoadMoreAdapterWrapper adapterWrapper) {
        this.mRecyclerView = recyclerView;
        this.mAdapterWrapper = adapterWrapper;
        mLayoutManager = mRecyclerView.getLayoutManager();
        checkFullScreen(mRecyclerView);
        init();
    }

    private void init() {
        mRecyclerView.addOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (!mAdapterWrapper.isEnableLoadMore()) {
            return;
        }
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (isLoading) {
                return;
            }
            if (mLayoutManager instanceof LinearLayoutManager) {
                int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
                lastItemCount = mLayoutManager.getItemCount();
                if (!isFinish && lastCompletelyVisibleItemPosition == lastItemCount - 2) {
                    int firstCompletelyVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition();
                    View viewByPosition = mLayoutManager.findViewByPosition(lastCompletelyVisibleItemPosition);
                    if (null == viewByPosition) {
                        return;
                    }
                    int i = recyclerView.getBottom() - recyclerView.getPaddingBottom() - viewByPosition.getBottom();
                    if (i > 0 && firstCompletelyVisibleItemPosition != 0) {
                        recyclerView.smoothScrollBy(0, -i);
                    }
                } else if (!isFinish && lastCompletelyVisibleItemPosition == lastItemCount - 1) {
                    isLoading = true;
                    mAdapterWrapper.setLoadMoreState(true, false);
                    if (null != onLoadMoreListener) {
                        onLoadMoreListener.loadMore();
                    }
                }
            }
        }
    }

    /**
     * 加载完了调用此方法来关闭加载更多View或设置没有更多数据的View
     * @param isFinish
     */
    public void setLoadMoreComplete(boolean isFinish) {
        isLoading = false;
        this.isFinish = isFinish;
        if (mLayoutManager.getItemCount() == lastItemCount) {//处理加载更多时没数据增加的情况，隐藏加载更多条目
            int lastCompletelyVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition();
            if (lastCompletelyVisibleItemPosition < lastItemCount - 2) {
                return;
            }
            int firstCompletelyVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition();
            View viewByPosition = mLayoutManager.findViewByPosition(lastItemCount - 2);
            int i = mRecyclerView.getBottom() - mRecyclerView.getPaddingBottom() - viewByPosition.getBottom();
            if (i > 0 && firstCompletelyVisibleItemPosition != 0) {
                mRecyclerView.smoothScrollBy(0, -i);
            }
        }
        mAdapterWrapper.setLoadMoreState(false, isFinish);
    }

    /**
     * 检测数据是否满屏
     * @param recyclerView
     */
    private void checkFullScreen(RecyclerView recyclerView) {
        mAdapterWrapper.setLoadMoreEnable(false);
        if (recyclerView == null) return;
        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) return;
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (manager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
                    mAdapterWrapper.setLoadMoreEnable(isFullScreen(linearLayoutManager));
                } else if (manager instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) manager;
                    int[] positions = new int[staggeredGridLayoutManager.getSpanCount()];
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(positions);
                    int pos = getTheBiggestNumber(positions) + 1;
                    mAdapterWrapper.setLoadMoreEnable(pos != mAdapterWrapper.getItemCount());
                }
            }
        }, 50);
    }

    private boolean isFullScreen(LinearLayoutManager layoutManager) {
        return (layoutManager.findLastCompletelyVisibleItemPosition() + 1) != mAdapterWrapper.getItemCount() ||
                layoutManager.findFirstCompletelyVisibleItemPosition() != 0;
    }

    private int getTheBiggestNumber(int[] numbers) {
        int tmp = -1;
        if (numbers == null || numbers.length == 0) {
            return tmp;
        }
        for (int num : numbers) {
            if (num > tmp) {
                tmp = num;
            }
        }
        return tmp;
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }
}
