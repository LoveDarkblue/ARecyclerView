package com.lcp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by Aislli on 2018/9/4 0004.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_CONTENT_VIEW = 100001;
    public static final int TYPE_FOOTER_VIEW = 100002;
    public static final int TYPE_HEADER_VIEW = 100003;
    private final int DEFAULT_LOADING_VIEW = R.layout.item_load_more;
    private final int DEFAULT_FAILED_VIEW = R.layout.item_load_failed;
    private final int DEFAULT_END_VIEW = R.layout.item_load_end;
    private final int STATE_DEFAULT = 10000;//正常状态
    private final int STATE_LOADING = 10001;//加载中状态
    private final int STATE_FAILED = 10002;//加载失败状态
    private final int STATE_END = 10003;//没有更多数据了

    protected List<T> mDatas;
    private boolean isOpenLoadMore;
    private RelativeLayout mFooterLayout;
    private OnLoadMoreListener onLoadMoreListener;
    private View mLoadingView; //更多数据加载中view
    private View mLoadFailedView;//更多数据加载失败View
    private View mLoadEndView;//没有更多数据了
    private Context mContext;
    private TextView mLoadingViewTv;
    private View mLoadingViewPb;
    private int currentState;
    private RecyclerView mRecyclerView;
    private LinearLayout mHeaderLayout;
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        isOpenLoadMore = true;
    }

    public BaseAdapter(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    protected abstract int getLayoutId();

    protected abstract void convert(BaseViewHolder viewHolder, T item);

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = null;
        switch (viewType) {
            case TYPE_CONTENT_VIEW:
                baseViewHolder = BaseViewHolder.create(getLayoutId(), parent);
                break;
            case TYPE_FOOTER_VIEW:
                if (mFooterLayout == null) {
                    mFooterLayout = new RelativeLayout(parent.getContext());
                }
                baseViewHolder = BaseViewHolder.create(mFooterLayout);
                break;
            case TYPE_HEADER_VIEW:
                baseViewHolder = BaseViewHolder.create(mHeaderLayout);
                break;
        }
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_CONTENT_VIEW:
                final BaseViewHolder viewHolder = (BaseViewHolder) holder;
                convert(viewHolder, mDatas.get(position - getHeaderViewCount()));
                if (null != onItemClickListener) {
                    View item_content = viewHolder.itemView.findViewWithTag("item_content");
                    check_item_content(item_content);
                    item_content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(v, viewHolder.getAdapterPosition());
                        }
                    });
                }
                if (null != onItemLongClickListener) {
                    View item_content = viewHolder.itemView.findViewWithTag("item_content");
                    check_item_content(item_content);
                    item_content.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return onItemLongClickListener.onItemLongClick(v, viewHolder.getAdapterPosition());
                        }
                    });
                }
                break;
        }
    }

    private void check_item_content(View item_content) {
        if (null == item_content) {
            throw new IllegalArgumentException("Your adapter item should be add the tag 'item_content' to set onItemClickListener or onItemLongClickListener!");
        }
    }

    @Override
    public int getItemCount() {
        if (null == mDatas) {
            return 0;
        }
        return mDatas.size() + getFooterViewCount() + getHeaderViewCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterView(position)) {
            return TYPE_FOOTER_VIEW;
        }
        if (isHeaderView(position)) {
            return TYPE_HEADER_VIEW;
        }
        return TYPE_CONTENT_VIEW;
    }

    private boolean isFooterView(int position) {
        return isOpenLoadMore && position >= getItemCount() - 1;
    }

    protected int getFooterViewCount() {
        return isOpenLoadMore && !mDatas.isEmpty() ? 1 : 0;
    }

    private boolean isHeaderView(int position) {
        return position < getHeaderViewCount();
    }

    protected int getHeaderViewCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    public void addHeaderView(View view) {
        addHeaderView(view, 0);
    }

    /**
     * 添加headerView
     *
     * @param view
     * @param index
     */
    public void addHeaderView(View view, int index) {
        checkHeaderLayout(view);
        int childCount = mHeaderLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderLayout.addView(view, index);
        if (mHeaderLayout.getChildCount() == 1) {
            notifyItemInserted(0);
        }
    }

    public void removeHeaderView(View header) {
        if (getHeaderViewCount() == 0) return;

        mHeaderLayout.removeView(header);
        if (mHeaderLayout.getChildCount() == 0) {
            notifyItemRemoved(0);
        }
    }

    private void checkHeaderLayout(View view) {
        if (null == mHeaderLayout) {
            mHeaderLayout = new LinearLayout(view.getContext());
            mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
            mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (isFooterView(holder.getLayoutPosition()) || isHeaderView(holder.getLayoutPosition())) {
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
        this.mContext = recyclerView.getContext();
        mRecyclerView = recyclerView;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position) || isHeaderView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
        initLoadMore(recyclerView, layoutManager);
    }

    private void initLoadMore(RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager) {
        if (!isOpenLoadMore || onLoadMoreListener == null) {
            return;
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isOpenLoadMore || onLoadMoreListener == null) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItemPosition = findLastVisibleItemPosition(layoutManager);
                    if (lastVisibleItemPosition < 0) {
                        return;
                    }
                    if (lastVisibleItemPosition + 2 == getItemCount() && currentState != STATE_END) {
                        if (findFirstVisibleItemPosition(layoutManager) == 0) return;
                        View viewByPosition = layoutManager.findViewByPosition(lastVisibleItemPosition);
                        if (null == viewByPosition) return;
                        int i = recyclerView.getBottom() - recyclerView.getPaddingBottom() - viewByPosition.getBottom();
                        if (i > 0) {
                            recyclerView.smoothScrollBy(0, -i);
                        }
                    } else if (lastVisibleItemPosition + 1 == getItemCount()) {
                        scrollLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        setLoadingView(DEFAULT_LOADING_VIEW);
        setLoadFailedView(DEFAULT_FAILED_VIEW);
        setLoadEndView(DEFAULT_END_VIEW);
        checkFullScreen(recyclerView);
    }

    private void checkFullScreen(final RecyclerView recyclerView) {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFullScreen(recyclerView)) {
//                    loadEnd();//因为增加了删除功能，改成不满一屏就去掉footer防止用户把列表给删完了出现的奇怪现象
                    stateNoScreen();
                }
            }
        }, 50);
    }

    private void scrollLoadMore() {
        if (mFooterLayout.getChildAt(0) == mLoadingView && currentState == STATE_DEFAULT) {
            if (onLoadMoreListener != null) {
                stateLoading();
                onLoadMoreListener.onLoadMore(false);
            }
        }
    }

    private int findFirstVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null);
            return getTheSmallestNumber(lastVisibleItemPositions);
        }
        return -1;
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null);
            return getTheBiggestNumber(lastVisibleItemPositions);
        }
        return -1;
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

    private int getTheSmallestNumber(int[] numbers) {
        int tmp = 0;
        if (numbers == null || numbers.length == 0) {
            return tmp;
        }
        for (int num : numbers) {
            if (num < tmp) {
                tmp = num;
            }
        }
        return tmp;
    }

    /**
     * 数据是否满屏了
     *
     * @param recyclerView
     * @return
     */
    private boolean isFullScreen(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        return manager != null && (findLastVisibleItemPosition(manager) + 1 != getItemCount() || findFirstVisibleItemPosition(manager) != 0);
    }

    private void addFooterView(View footerView) {
        if (footerView == null) {
            return;
        }

        if (mFooterLayout == null) {
            mFooterLayout = new RelativeLayout(footerView.getContext());
        }
        removeFooterView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mFooterLayout.addView(footerView, params);
    }

    private void removeFooterView() {
        mFooterLayout.removeAllViews();
    }

    /**
     * 初始化加载中布局
     *
     * @param loadingView
     */
    public void setLoadingView(View loadingView) {
        mLoadingView = loadingView;
        mLoadingViewTv = mLoadingView.findViewById(R.id.ilm_tv);
        mLoadingViewPb = mLoadingView.findViewById(R.id.ilm_pb);
        stateDefault();
        addFooterView(mLoadingView);
    }

    public void setLoadingView(int loadingId) {
        setLoadingView(inflate(mContext, loadingId));
    }

    /**
     * 初始加载失败布局
     *
     * @param loadFailedView
     */
    public void setLoadFailedView(View loadFailedView) {
        mLoadFailedView = loadFailedView;
    }

    public void setLoadFailedView(int loadFailedId) {
        setLoadFailedView(inflate(mContext, loadFailedId));
    }

    /**
     * 初始化全部加载完成布局
     *
     * @param loadEndView
     */
    public void setLoadEndView(View loadEndView) {
        mLoadEndView = loadEndView;
    }

    public void setLoadEndView(int loadEndId) {
        setLoadEndView(inflate(mContext, loadEndId));
    }

    private static View inflate(Context context, int layoutId) {
        if (layoutId <= 0) {
            return null;
        }
        return LayoutInflater.from(context).inflate(layoutId, null);
    }

    /**
     * 不满一屏时
     */
    private void stateNoScreen() {
        if (!isOpenLoadMore) {
            return;
        }
        currentState = STATE_END;
        isOpenLoadMore = false;
        removeFooterView();
        notifyItemRemoved(getItemCount());
    }

    /**
     * 数据加载完成
     */
    public void loadEnd() {
        currentState = STATE_END;
        if (mLoadEndView != null) {
            addFooterView(mLoadEndView);
        } else {
            addFooterView(new View(mContext));
        }
    }

    /**
     * 数据加载失败
     */
    public void loadFailed() {
        currentState = STATE_FAILED;
        addFooterView(mLoadFailedView);
        mLoadFailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFooterView(mLoadingView);
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore(true);
                }
            }
        });
    }

    /**
     * 加载更多状态
     */
    private void stateLoading() {
        currentState = STATE_LOADING;
        mLoadingViewTv.setText("正在努力加载...");
        mLoadingViewPb.setVisibility(View.VISIBLE);
    }

    /**
     * 重置成正常状态
     */
    private void stateDefault() {
        currentState = STATE_DEFAULT;
        mLoadingViewTv.setText("上拉加载更多");
        mLoadingViewPb.setVisibility(View.GONE);
//        mRecyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hiddenFooterView();
//            }
//        }, 200);
    }

    protected void hiddenFooterView() {
        if (!isOpenLoadMore) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        int lastVisibleItemPosition = findLastVisibleItemPosition(layoutManager);
        if (lastVisibleItemPosition >= getItemCount() - 2 && currentState != STATE_END) {
            //之前写的是==0，现在改成<=1，因为一直滑动删除到数据不满一屏，当删到footer即将出来的那个临界点时，不一定能让第一个item完全显示，<=1会更稳
            if (findFirstVisibleItemPosition(layoutManager) <= 1) {
                //不足一屏时隐藏footer
                stateNoScreen();
                return;
            }
            View viewByPosition = layoutManager.findViewByPosition(getItemCount() - 2);
            if (null == viewByPosition) return;
            int i = mRecyclerView.getBottom() - mRecyclerView.getPaddingBottom() - viewByPosition.getBottom();
            if (i > 0) {
                mRecyclerView.smoothScrollBy(0, -i);
            }
        }
    }

    /**
     * 上拉加载更多的数据
     *
     * @param datas
     */
    public void addLoadMoreData(List<T> datas) {
        int size = mDatas.size();
        mDatas.addAll(datas);
        notifyItemInserted(size + getHeaderViewCount());
        stateDefault();
    }

    public void addLoadMoreData(T data) {
        int size = mDatas.size();
        mDatas.add(data);
        notifyItemInserted(size + getHeaderViewCount());
        stateDefault();
    }

    /**
     * 下拉刷新，得到的新数据插入到原数据头部
     *
     * @param datas
     */
    public void addRefreshData(List<T> datas) {
        mDatas.addAll(0, datas);
        notifyItemRangeInserted(getHeaderViewCount(), datas.size() + getHeaderViewCount());
        mRecyclerView.scrollToPosition(0);
    }

    public void addRefreshData(T data) {
        mDatas.add(0, data);
        notifyItemInserted(getHeaderViewCount());
        mRecyclerView.scrollToPosition(0);
    }

    /**
     * 替换全部旧数据
     *
     * @param datas
     */
    public void setNewData(List<T> datas) {
        stateDefault();
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }
}
