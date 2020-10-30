package com.github.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

/***
 *   created by android on 2019/4/29
 */
public abstract class LoadMoreAdapter<T> extends CustomAdapter<T> implements LoadListener {
    private long click_interval = 900; // 阻塞时间间隔
    private long lastClickTime;
    private final int view_type_load = 70000;

    private View loadView;
    private View errorView;
    private View noMoreView;


    /*显示加载更多*/
    public static final int status_load = 8000;
    /*暂无更多数据*/
    public static final int status_no_more = 8001;
    /*加载失败*/
    public static final int status_error = 8002;
    /*不显示加载view*/
    public static final int status_default = 8003;

    private int currentStatus = status_default;
    /*** 是否隐藏暂无内容的提示*/
    private boolean isHiddenNoMoreView = false;
    private String loadViewText = "正在加载更多...";
    private String noMoreViewText = "暂无更多";
    private String errorViewText = "加载失败,点击重试";
    private int loadViewHeight = 40;

    private int bottomViewBackground = Color.TRANSPARENT;

    /*是否请求完成，防止上滑下滑重复触发请求*/
    private boolean isEndRequest = true;

    /*回调方法,触发加载更多*/
    public OnLoadMoreListener onLoadMoreListener;

    @Override
    public void loadHasMore(boolean hasMore) {
        loadHasMore(hasMore, false);
    }

    @Override
    public void loadHasMore(boolean hasMore, boolean hiddenNoMoreView) {
        setHiddenNoMoreView(hiddenNoMoreView);
        setCurrentStatus(hasMore ? status_load : status_no_more);
        completeRequest();
        if(!hasMore){
            int size = getDataCount() + getHeaderCount() + getFooterCount();
            if(hiddenNoMoreView){
                this.notifyItemRemoved(size);
            }else{
                notifyItemChanged(size);
            }
        }
    }


    @Override
    public void loadError() {
        setCurrentStatus(status_error);
        completeRequest();
        notifyItemChanged(getItemCount()-1);
    }

    public interface OnLoadMoreListener {
        void loadMore(LoadMoreAdapter adapter);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public LoadMoreAdapter(int layoutId) {
        super(layoutId);
        loadView = getLoadView();
        errorView = getErrorView();
        noMoreView = getNoMoreView();

        loadViewText = getLoadViewText();
        noMoreViewText = getNoMoreViewText();
        errorViewText = getErrorViewText();
        loadViewHeight = getLoadViewHeight();
        bottomViewBackground = getBottomViewBackground();

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
        if ((isHiddenNoMoreView() && status_no_more == currentStatus) || currentStatus == status_default) {
            /*不显示底部loadview*/
            return size;
        }
        /*显示底部loadview*/
        return size + 1;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (isLoadViewType(viewType)) {
            CustomViewHolder viewHolder = new CustomViewHolder(getLoadStatusView(viewGroup.getContext()));
            if (currentStatus == status_error) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                });
            }
            return viewHolder;
        }

        return super.onCreateViewHolder(viewGroup, viewType);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, int position) {
        if (isLoadViewType(getItemViewType(position))) {
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

    public View getLoadView() {
        return loadView;
    }

    public View getErrorView() {
        return errorView;
    }

    public View getNoMoreView() {
        return noMoreView;
    }

    private View getLoadStatusView(Context context) {
        View view = null;
        switch (currentStatus) {
            case status_load:
                /*显示加载更多*/
                if (getLoadView() != null) {
                    view = getLoadView();
                } else {
                    view = getDefaultView(context, loadViewText);
                }
                break;
            case status_no_more:
                /*暂无更多数据*/
                if (getNoMoreView() != null) {
                    view = getLoadView();
                } else {
                    view = getDefaultView(context, noMoreViewText);
                }
                break;
            case status_error:
                /*加载失败*/
                if (getErrorView() != null) {
                    view = getLoadView();
                } else {
                    view = getDefaultView(context, errorViewText);
                }
                break;
        }
        return view;
    }

    private TextView getDefaultView(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(context, loadViewHeight));
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(bottomViewBackground);
        textView.setText(text);
        return textView;
    }


    private int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale);
    }


    private void completeRequest() {
        isEndRequest = true;
    }

    @Override
    protected int getBottomViewCount() {
        int loadViewSize = 1;
        if ((isHiddenNoMoreView() && status_no_more == currentStatus) || currentStatus == status_default) {
            /*不显示底部loadview*/
            loadViewSize = 0;
        }
        return super.getBottomViewCount() + loadViewSize;
    }

    private void setCurrentStatus(int currentStatus) {
        this.currentStatus = currentStatus;
    }

    public int getCurrentStatus() {
        return currentStatus;
    }

    public void setLoadView(View loadView) {
        this.loadView = loadView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public void setNoMoreView(View noMoreView) {
        this.noMoreView = noMoreView;
    }

    public void setHiddenNoMoreView(boolean hiddenNoMoreView) {
        isHiddenNoMoreView = hiddenNoMoreView;
    }

    public void setLoadViewText(String loadViewText) {
        this.loadViewText = loadViewText;
    }

    public void setNoMoreViewText(String noMoreViewText) {
        this.noMoreViewText = noMoreViewText;
    }

    public void setErrorViewText(String errorViewText) {
        this.errorViewText = errorViewText;
    }

    public void setLoadViewHeight(int loadViewHeight) {
        this.loadViewHeight = loadViewHeight;
    }

    public void setBottomViewBackground(@ColorInt int bottomViewBackground) {
        this.bottomViewBackground = bottomViewBackground;
    }

    public boolean isHiddenNoMoreView() {
        return isHiddenNoMoreView;
    }

    public String getLoadViewText() {
        return loadViewText;
    }

    public String getNoMoreViewText() {
        return noMoreViewText;
    }

    public String getErrorViewText() {
        return errorViewText;
    }

    public int getLoadViewHeight() {
        return loadViewHeight;
    }

    public int getBottomViewBackground() {
        return bottomViewBackground;
    }

    @Override
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
    }

    @Override
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
    }

    public int getNotLoadViewCount() {
        return getDataCount() + getHeaderCount() + getFooterCount();
    }
}
