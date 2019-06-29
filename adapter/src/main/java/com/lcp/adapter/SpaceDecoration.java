package com.lcp.adapter;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Aislli on 2018/9/10 0010.
 */
public class SpaceDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int spanCount;
        int orientation;
        int itemCount = layoutManager.getItemCount();
        boolean isLoadMore;
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            spanCount = gridLayoutManager.getSpanCount();
            orientation = gridLayoutManager.getOrientation();

            GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
            isLoadMore = lp.getSpanSize() == spanCount;
        } else {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            spanCount = staggeredGridLayoutManager.getSpanCount();
            orientation = staggeredGridLayoutManager.getOrientation();

            StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            isLoadMore = lp.isFullSpan();
        }
        int childAdapterPosition = parent.getChildAdapterPosition(view);

        if (orientation == GridLayoutManager.VERTICAL) {
            outRect.top = childAdapterPosition < spanCount ? 0 : space;// the first row
            outRect.bottom = 0;
            outRect.left = 0;
            if (childAdapterPosition % spanCount < (spanCount - 1)) {// except the last column
                outRect.right = space;
            }
            if (childAdapterPosition == itemCount - 1 && isLoadMore) {// load more view
                outRect.right = 0;
            }
        }
    }
}
