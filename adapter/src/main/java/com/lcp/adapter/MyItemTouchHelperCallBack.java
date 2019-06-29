package com.lcp.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * Created by Aislli on 2019/4/5 0008.
 */
public class MyItemTouchHelperCallBack extends ItemTouchHelper.Callback {
    private OnItemTouchHelperCallBack onItemTouchHelperCallBack;

    private Rect rect = new Rect();
    private Paint mPaint = new Paint();

    public MyItemTouchHelperCallBack(OnItemTouchHelperCallBack onItemTouchHelperCallBack) {
        this.onItemTouchHelperCallBack = onItemTouchHelperCallBack;
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(15f);
    }

    /**
     * 设置允许拖动换位和滑动删除的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (isContentType(viewHolder)) {
            //dragFlags:设置为可以向上和向下拖动
            //swipeFlags:设置成可以从左向右滑动删除
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.END);
        }
        return makeMovementFlags(0, 0);
    }

    private boolean isContentType(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getItemViewType() == BaseAdapter.TYPE_CONTENT_VIEW;
    }

    /**
     * 返回拖拽中换位的回调
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return true:换位成功
     * false:没换成功
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (null == onItemTouchHelperCallBack) {
            return false;
        }
        return onItemTouchHelperCallBack.onMove(viewHolder, target);
    }

    /**
     * 滑动后的回调
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (null != onItemTouchHelperCallBack) {
            onItemTouchHelperCallBack.onSwiped(viewHolder);
        }
    }

    /**
     * 成功换位的回调（无特殊需求一般无需处理）
     *
     * @param recyclerView
     * @param viewHolder
     * @param fromPos
     * @param target
     * @param toPos
     * @param x
     * @param y
     */
    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
    }

    /**
     * 拖拽或滑动删除时，在item下面画点什么时用到
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //滑动删除时在底层画文字告知用户此为删除操作
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextSize(40f);
            c.drawText("delete", 10, itemView.getY() + mPaint.getTextSize() + (itemView.getHeight() - mPaint.getTextSize()) / 2, mPaint);
        }
    }

    /**
     * 拖拽或滑动删除时，在item上面画点什么时用到
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (isCurrentlyActive) {
                //拖动时画个框
                mPaint.setStyle(Paint.Style.STROKE);
                rect.set((int) mPaint.getStrokeWidth() / 2, (int) (itemView.getTop() + dY), (int) (itemView.getRight() - mPaint.getStrokeWidth() / 2), (int) (itemView.getBottom() + dY));
                c.drawRect(rect, mPaint);
            } else {
                //擦除拖动时画的框
                c.save();
                c.clipRect(rect);
                c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.DST_OVER);
                c.restore();
            }
        }
    }

    /**
     * 拖动或滑动完回调
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }

    /**
     * 是否允许滑动删除，默认允许
     *
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    /**
     * 是否允许拖拽，默认允许
     *
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    public interface OnItemTouchHelperCallBack {
        boolean onMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);

        void onSwiped(RecyclerView.ViewHolder viewHolder);
    }
}
