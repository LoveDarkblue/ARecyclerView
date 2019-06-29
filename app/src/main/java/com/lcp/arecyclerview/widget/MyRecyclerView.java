package com.lcp.arecyclerview.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Aislli on 2019/3/18 0018.
 */
public class MyRecyclerView extends RecyclerView {
    int mLastTouchPosition = -1;
    private ScrollerLayout mLastTouchItem;

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        boolean intercept = super.onInterceptTouchEvent(ev);
        if (action == MotionEvent.ACTION_DOWN) {
            intercept = false;//1.父View默认不在Down时拦截
            float x = ev.getX();
            float y = ev.getY();
            int childAdapterPosition = getChildAdapterPosition(findChildViewUnder(x, y));
            //3.当这次点击的item和上次不是同一个item,且上一个点击的item是ScrollerLayout类型，并且这个滑动菜单是开着的，就关掉菜单并执行RecyclerView的操作
            if (childAdapterPosition != mLastTouchPosition && null != mLastTouchItem && mLastTouchItem.isOpen()) {
                mLastTouchItem.close();
                intercept = true;
            }
            if (intercept) {
                mLastTouchPosition = -1;
                mLastTouchItem = null;
            } else {
                mLastTouchPosition = childAdapterPosition;
                ViewHolder viewHolderForAdapterPosition = findViewHolderForAdapterPosition(childAdapterPosition);
                //2.如果点击的条目是ScrollerLayout类型（支持滑动菜单的item），就赋值记录
                if (null != viewHolderForAdapterPosition && viewHolderForAdapterPosition.itemView instanceof ScrollerLayout) {
                    mLastTouchItem = (ScrollerLayout) viewHolderForAdapterPosition.itemView;
                }
            }
        }
        return intercept;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
            //滑动时关掉菜单
            if (null != mLastTouchItem && mLastTouchItem.isOpen()) {
                mLastTouchItem.close();
            }
        }
    }
}
