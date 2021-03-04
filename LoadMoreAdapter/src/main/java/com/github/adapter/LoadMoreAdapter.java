package com.github.adapter;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/***
 *   created by android on 2019/4/29
 */
public abstract class LoadMoreAdapter<T> extends CustomAdapter<T> implements LoadListener {
    private long click_interval = 900; // 阻塞时间间隔
    private long lastClickTime;
    private final int view_type_load = 70000;

    private LoadView loadLayout;

    /*显示加载更多*/
    public static final int status_load = 8000;
    /*暂无更多数据*/
    public static final int status_no_more = 8001;
    /*加载失败*/
    public static final int status_error = 8002;

    private int currentStatus = status_no_more;
    /*** 是否隐藏暂无内容的提示*/
    private boolean isHiddenNoMoreView = false;

    /*是否请求完成，防止上滑下滑重复触发请求*/
    private boolean isEndRequest = true;

    /*回调方法,触发加载更多*/
    public OnLoadMoreListener onLoadMoreListener;

    @Override
    public void loadHasMore(boolean hasMore) {
        loadHasMore(hasMore, false);
    }

    @Override
    public void loadHasMore(final boolean hasMore,final boolean hiddenNoMoreView) {
        HandlerUtils.get().post(new Runnable() {
            @Override
            public void run() {
                /*下拉刷新时，notifyItemChanged崩溃，可用handler post执行*/
                setHiddenNoMoreView(hiddenNoMoreView);
                setCurrentStatus(hasMore ? status_load : status_no_more);
                completeRequest();
                int size = getDataCount() + getHeaderCount() + getFooterCount();
                if (!hasMore) {
                    if (hiddenNoMoreView) {
                        notifyItemRemoved(size);
                    } else {
                        notifyItemChanged(size);
                    }
                } else {
                    notifyItemChanged(size);
                }
            }
        });
    }


    @Override
    public void loadError() {
        setCurrentStatus(status_error);
        completeRequest();
        notifyItemChanged(getItemCount() - 1);
    }

    public interface OnLoadMoreListener {
        void loadMore(LoadListener loadListener);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public LoadMoreAdapter(int layoutId) {
        super(layoutId);
    }

    public void setLoadLayout(LoadView loadLayout) {
        this.loadLayout = loadLayout;
    }
    public LoadView getLoadLayout() {
        if (loadLayout == null) {
            loadLayout=new LoadView();
        }
        return loadLayout;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= getNotLoadViewCount()) {
            return view_type_load;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        int size = getDataCount() + getHeaderCount() + getFooterCount();
        if ((isHiddenNoMoreView() && status_no_more == currentStatus)) {
            /*不显示底部loadview*/
            return size;
        }
        /*显示底部loadview*/
        return size + 1;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (isLoadViewType(viewType)) {
            CustomViewHolder viewHolder = new CustomViewHolder(getLoadLayout().getLayout(viewGroup.getContext(), getCurrentStatus()));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentStatus == status_error) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        long interval = currentTime - lastClickTime;
                        if (interval >= click_interval) {
                            //改变状态会触发加载，所以这里不需要手动调用
                            setCurrentStatus(status_load);
                            isEndRequest = true;
                            notifyItemChanged(getItemCount() - 1);
                        }
                        lastClickTime = currentTime;
                    }
                }
            });

            return viewHolder;
        }

        return super.onCreateViewHolder(viewGroup, viewType);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, int position) {
        if (isLoadViewType(getItemViewType(position))) {
            getLoadLayout().changeLoadView((ViewGroup) viewHolder.itemView, getCurrentStatus());
            if (getCurrentStatus() == status_load && position != 0) {
                //如果postion等于0，只有loadmoreview的时候,
                // 防止adapter初始化还没真正设置数据时自动加载更多
                loadMoreData();
            }
            return;
        }
        super.onBindViewHolder(viewHolder, position);
    }

    /*是否是加载view*/
    private boolean isLoadViewType(int viewType) {
        return viewType == view_type_load;
    }

    private void loadMoreData() {
        if (isEndRequest && onLoadMoreListener != null) {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    isEndRequest = false;
                    onLoadMoreListener.loadMore(LoadMoreAdapter.this);
                }
            });
        }
    }

    private Handler getHandler() {
        return HandlerUtils.get();
    }

    protected void completeRequest() {
        isEndRequest = true;
    }

    @Override
    protected int getBottomViewCount() {
        int loadViewSize = 1;
        if ((isHiddenNoMoreView() && status_no_more == currentStatus)) {
            /*不显示底部loadview*/
            loadViewSize = 0;
        }
        return super.getBottomViewCount() + loadViewSize;
    }

    protected void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setHiddenNoMoreView(boolean hiddenNoMoreView) {
        isHiddenNoMoreView = hiddenNoMoreView;
    }

    public boolean isHiddenNoMoreView() {
        return isHiddenNoMoreView;
    }

    @Override
    protected boolean needSetSpanSize(int position) {
        int viewType = getItemViewType(position);
        return isLoadViewType(viewType);
    }

    /*@Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (isLoadViewType(viewType)) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
        }
    }*/
    @Override
    protected boolean needSetFullSpan(CustomViewHolder holder) {
        //如果不显示底部布局，则不用设置loadview跨列，或者不是最后一个item的position
        if (getNotLoadViewCount() >= getItemCount() || holder.getAdapterPosition() != getItemCount() - 1) {
            return false;
        }
        return true;
    }

   /* @Override
    public void onViewAttachedToWindow(@NonNull CustomViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //如果不显示底部布局，则不用设置loadview跨列，或者不是最后一个item的position
        if (getNotLoadViewCount() >= getItemCount() || holder.getAdapterPosition() != getItemCount() - 1) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams sglm = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            sglm.setFullSpan(true);
        }
    }*/

    public int getNotLoadViewCount() {
        return getDataCount() + getHeaderCount() + getFooterCount();
    }
}
