package com.github.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

public abstract class MyLoadMoreAdapter<T> extends MyBaseRecyclerAdapter<T> {
    protected NestedScrollView nsv;
    Handler handler;
    private final int header_view = 100000;
    private final int footer_view = 200000;
    /*正常view item*/
    private final int normal_view = 20000;
    /*显示加载更多*/
    private final int load_more_view_type = 10000;
    /*暂无更多数据*/
    private final int no_more_view_type = 10001;
    /*加载失败*/
    private final int load_error_view_type = 10002;
    /*回调方法,触发加载更多*/
    public OnLoadMoreListener onLoadMoreListener;
    /***用于判断是否还有更多数据*/
    private int pageSize;
    /***是否还有更多数据,没有更多数据显示"暂无更多"*/
    private boolean hasMoreData = false;
    /*** 加载是否失败,用于点击重新加载*/
    private boolean isLoadError;
    /*** 是否隐藏暂无内容的提示*/
    private boolean isHiddenPromptView = false;
    private View loadView, errorView, noMoreView;
    private String loadViewText = "正在加载更多...";
    private String noMoreViewText = "暂无更多";
    private String errorViewText = "加载失败,点击重试";
    private int loadViewHeight = 40;

    private int bottomViewBackground = -1;

    private boolean isEndLoad = true;


    protected SparseArrayCompat<View> headerViewList = new SparseArrayCompat<>();
    protected SparseArrayCompat<View> footerViewList = new SparseArrayCompat<>();

    protected OnHeaderClickListener mHeaderClickListener;
    protected OnHeaderLongClickListener mHeaderLongClickListener;

    public void setOnHeaderClickListener(OnHeaderClickListener listener) {
        mHeaderClickListener = listener;
    }

    public void setOnHeaderLongClickListener(OnHeaderLongClickListener listener) {
        mHeaderLongClickListener = listener;
    }

    public interface OnHeaderClickListener {
        public void onHeaderClick(View itemView, int position);
    }

    public interface OnHeaderLongClickListener {
        public void onHeaderLongClick(View itemView, int position);
    }


    protected OnFooterClickListener mFooterClickListener;
    protected OnFooterLongClickListener mFooterLongClickListener;

    public void setOnFooterClickListener(OnFooterClickListener listener) {
        mFooterClickListener = listener;
    }

    public void setOnFooterLongClickListener(OnFooterLongClickListener listener) {
        mFooterLongClickListener = listener;
    }

    public interface OnFooterClickListener {
        public void onFooterClick(View itemView, int position);
    }

    public interface OnFooterLongClickListener {
        public void onFooterLongClick(View itemView, int position);
    }


    public MyLoadMoreAdapter(Context mContext, int layoutId, int pageSize) {
        super(mContext, layoutId);
        this.pageSize = pageSize;
        getCustomViewStr();
        getCustomView();
    }
    public MyLoadMoreAdapter(Context mContext, int layoutId, int pageSize, NestedScrollView nestedScrollView) {
        super(mContext, layoutId);
        getCustomViewStr();
        getCustomView();
        this.pageSize = pageSize;
        this.nsv = nestedScrollView;
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //当RecyclerView嵌套NestedScrollView时引发无限自动加载
                //现在改为如何有NestedScrollView就判断是否滑动到底部，如果到底就加载数据
                //不过RecyclerView嵌套NestedScrollView使用，当数据量过多时会有卡顿效果
                if (nsv.canScrollVertically(1) == false) {
                    if (isEndLoad && hasMoreData && !isLoadError) {
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                isEndLoad = false;
                                onLoadMoreListener.loadMore();
                            }
                        });
                    }
                }
            }
        });

    }

    private void getCustomViewStr() {
        String loadText = getLoadViewText();
        if (loadText != null) {
            loadViewText=loadText;
        }
        String noMoreText = getNoMoreViewText();
        if (noMoreViewText != null) {
            noMoreViewText=noMoreText;
        }
        String errorText = getErrorViewText();
        if (errorViewText != null) {
            errorViewText=errorText;
        }
    }

    private void getCustomView() {
        View load = getLoadView();
        if (load != null) {
            loadView = load;
        }
        View error = getErrorView();
        if (error != null) {
            errorView = error;
        }
        View noMore = getNoMoreView();
        if (noMore != null) {
            noMoreView = noMore;
        }
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

    public String getLoadViewText() {
        return loadViewText;
    }

    public String getNoMoreViewText() {
        return noMoreViewText;
    }

    public String getErrorViewText() {
        return errorViewText;
    }



    public void setTestListSize(int testListSize) {
        this.testListSize = testListSize;
    }

    abstract public void bindData(MyRecyclerViewHolder holder, int position, T item);


    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return headerViewList.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return footerViewList.keyAt(position - getHeaderCount() - getDataCount());
        } else if (mList != null && onLoadMoreListener != null && position == getItemCount() - 1) {
            if (isLoadError) {
                return load_error_view_type;
            } else if (hasMoreData) {
                return load_more_view_type;
            } else if (!hasMoreData) {
                return no_more_view_type;
            }
        }
        return super.getItemViewType(position - getHeaderCount());
    }

    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final MyRecyclerViewHolder holder;
        if (headerViewList.get(viewType) != null) {
            holder = new MyRecyclerViewHolder(mContext, headerViewList.get(viewType));
            if (mHeaderClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHeaderClickListener.onHeaderClick(holder.itemView, viewType - header_view);
                    }
                });
            }
            if (mHeaderLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mHeaderLongClickListener.onHeaderLongClick(holder.itemView, viewType - header_view);
                        return true;
                    }
                });
            }
        } else if (footerViewList.get(viewType) != null) {
            holder = new MyRecyclerViewHolder(mContext, footerViewList.get(viewType));
            if (mFooterClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFooterClickListener.onFooterClick(holder.itemView, viewType - footer_view);
                    }
                });
            }
            if (mFooterLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mFooterLongClickListener.onFooterLongClick(holder.itemView, viewType - footer_view);
                        return true;
                    }
                });
            }
        } else if (viewType == load_error_view_type || viewType == load_more_view_type || viewType == no_more_view_type) {
            holder = new MyRecyclerViewHolder(mContext, setDefaultView(viewType));
        } else {
            holder = super.onCreateViewHolder(parent, viewType);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        } else if (isFooterViewPos(position)) {
            return;
        }
        //暂时不支持设置test数据的时候还具备加载功能(主要还是懒，没实现这个^-^)
        if (onLoadMoreListener != null && testListSize <= 0) {
            if (position <= getItemCount() - 2) {
                bindData(holder, position - getHeaderCount(), testListSize > 0 ? null : mList.get(position - getHeaderCount()));
                if (isEndLoad && hasMoreData && !isLoadError && position == getItemCount() - 2 && nsv == null) {
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            isEndLoad = false;
                            onLoadMoreListener.loadMore();
                        }
                    });
                }
            } else {
                holder.bottomView.setOnClickListener(null);
                switch (holder.getItemViewType()) {
                    case load_error_view_type:
                        holder.bottomView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                isLoadError = false;
                                hasMoreData = true;
                                holder.bottomView.removeAllViews();
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        isEndLoad = false;
                                        onLoadMoreListener.loadMore();
                                    }
                                });
                            }
                        });
                        break;
                }
            }
        } else {
            bindData(holder, position - getHeaderCount(), testListSize > 0 ? null : mList.get(position - getHeaderCount()));
        }

    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeaderCount();
    }

    private boolean isFooterViewPos(int position) {
        if (position >= getHeaderCount() + getDataCount() && position < getItemCount() - getLoadMoreViewCount()) {
            return true;
        } else {
            return false;
        }
    }


    private View setDefaultView(int viewType) {
        MyRecyclerViewHolder.BottomView bottomView = new MyRecyclerViewHolder.BottomView(mContext);
        if (bottomViewBackground != -1) {
            bottomView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.white));
        }
        bottomView.setGravity(Gravity.CENTER);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bottomView.setLayoutParams(layoutParams);

        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(bottomViewBackground);
        switch (viewType) {
            case load_more_view_type://加载更多view
                if (loadView != null) {
                    bottomView.addView(loadView);
                } else {
                    layoutParams.height = dip2px(mContext, loadViewHeight);
                    textView.setText(loadViewText);
                    bottomView.addView(textView);
                }
                break;
            case no_more_view_type://暂无更多view
                if (noMoreView != null) {
                    bottomView.addView(noMoreView);
                } else {
                    layoutParams.height = dip2px(mContext, loadViewHeight);
                    textView.setText(noMoreViewText);
                    bottomView.addView(textView);
                }
                if (isHiddenPromptView) {
                    layoutParams.height = 0;
                }
                break;
            case load_error_view_type://加载失败view
                if (errorView != null) {
                    bottomView.addView(errorView);
                } else {
                    layoutParams.height = dip2px(mContext, loadViewHeight);
                    textView.setText(errorViewText);
                    bottomView.addView(textView);
                }
                break;
        }
        return bottomView;
    }

    @Override
    public int getItemCount() {
        if (testListSize > 0) {
            return testListSize + getLoadMoreViewCount() + getHeaderCount() + getFooterCount();
        }
        return mList == null ? 0 : mList.size() + getLoadMoreViewCount() + getHeaderCount() + getFooterCount();
    }

    public void addHeaderView(View view) {
        headerViewList.put(headerViewList.size() + header_view, view);
    }

    public void addFooterView(View view) {
        footerViewList.put(footerViewList.size() + footer_view, view);
    }

    public SparseArrayCompat<View> getHeaderViewList() {
        return headerViewList;
    }

    public void setHeaderViewList(SparseArrayCompat<View> headerViewList) {
        this.headerViewList = headerViewList;
    }

    public int getHeaderCount() {
        return headerViewList == null ? 0 : headerViewList.size();
    }

    public SparseArrayCompat<View> getFooterViewList() {
        return footerViewList;
    }

    public void setFooterViewList(SparseArrayCompat<View> footerViewList) {
        this.footerViewList = footerViewList;
    }

    public int getFooterCount() {
        return footerViewList == null ? 0 : footerViewList.size();
    }

    public int getDataCount() {
        if (testListSize > 0) {
            return testListSize;
        } else {
            return mList == null ? 0 : mList.size();
        }
    }

    @Override
    public void setList(List<T> list) {
        setList(list, false);
    }

    @Override
    public void setList(List<T> list, boolean isNotifyData) {
        this.isLoadError = false;
        if (list == null || list.size() == 0 || list.size() < pageSize) {
            hasMoreData = false;
        } else {
            hasMoreData = true;
        }
        this.mList = list;
        isEndLoad = true;
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void addList(List<T> list) {
        addList(list, false);
    }

    @Override
    public void addList(List<T> list, boolean isNotifyData) {
        if (this.mList == null) {
            this.mList = new ArrayList<>();
        }
        if (list == null || list.size() == 0) {
            hasMoreData = false;
        } else if (list.size() < pageSize) {
            hasMoreData = false;
            this.mList.addAll(list);
        } else {
            hasMoreData = true;
            this.mList.addAll(list);
        }
        isEndLoad = true;
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }

    public List<T> getList() {
        return mList;
    }

    public int getLoadMoreViewCount() {
        return onLoadMoreListener == null ? 0 : 1;
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    /*是否隐藏底部暂无内容的view*/
    public void setHiddenPromptView(boolean hiddenPromptView) {
        setHiddenPromptView(hiddenPromptView, false);
    }

    public void setHiddenPromptView() {
        setHiddenPromptView(true, false);
    }

    public void setHiddenPromptView(boolean hiddenPromptView, boolean isNotifyData) {
        isHiddenPromptView = hiddenPromptView;
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }

    /*是否加载失败*/
    public void setLoadError() {
        setLoadError(true);
    }

    public void setLoadError(boolean isNotifyData) {
        this.isLoadError = true;
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }

    /*是否还有更多数据*/
    public void setNoMoreData() {
        setNoMoreData(true);
    }

    public void setNoMoreData(boolean isNotifyData) {
        this.hasMoreData = false;
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }

    /*设置正在加载的view*/
    public void setLoadView(View loadView) {
        this.loadView = loadView;
    }

    /*设置加载失败的view*/
    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    /*设置没有更多数据的view*/
    public void setNoMoreView(View noMoreView) {
        this.noMoreView = noMoreView;
    }

    /*默认BottomView的情况下，设置正在加载的文字*/
    public void setLoadViewText(String loadViewText) {
        this.loadViewText = loadViewText;
    }

    /*默认BottomView的情况下，设置加载失败的文字*/
    public void setErrorViewText(String errorViewText) {
        this.errorViewText = errorViewText;
    }

    /*默认BottomView的情况下，设置没有更多数据的文字*/
    public void setNoMoreViewText(String noMoreViewText) {
        this.noMoreViewText = noMoreViewText;
    }

    public void setLoadViewHeight(int loadViewHeight) {
        this.loadViewHeight = loadViewHeight;
    }

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public int getBottomViewBackground() {
        return bottomViewBackground;
    }

    public void setBottomViewBackground(@ColorRes int bottomViewBackground) {
        this.bottomViewBackground = ContextCompat.getColor(mContext, bottomViewBackground);
    }

    public void setBottomViewBackground(String bottomViewBackground) {
        this.bottomViewBackground = Color.parseColor(bottomViewBackground);
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
                    if (headerViewList.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    } else if (footerViewList.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    } else if (viewType == load_more_view_type || viewType == no_more_view_type || viewType == load_error_view_type) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }
}
