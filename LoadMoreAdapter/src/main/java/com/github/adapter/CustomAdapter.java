package com.github.adapter;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.adapter.listener.AdapterOnClickListener;

import java.util.ArrayList;
import java.util.List;

/***
 *   created by android on 2019/4/29
 */
public abstract class CustomAdapter<T> extends RecyclerView.Adapter<CustomViewHolder> {

    protected List<T> mList;
    protected LayoutInflater mInflater;
    protected int layoutId;

    protected AdapterOnClickListener mClickListener;
    protected AdapterOnClickListener mLongClickListener;


    private final int view_type_header = 50000;
    private final int view_type_footer = 60000;
    protected SparseArrayCompat<View> headerView;
    protected SparseArrayCompat<View> footerView;


    protected AdapterOnClickListener headerClickListener;
    protected AdapterOnClickListener headerLongClickListener;
    protected AdapterOnClickListener footerClickListener;
    protected AdapterOnClickListener footerLongClickListener;


    public abstract void bindData(CustomViewHolder holder, int position, T item);
    public void bindDataByNull(CustomViewHolder holder, int position){};

    public View getCustomView() {
        return null;
    }

    public CustomAdapter(int layoutId) {
        this.layoutId = layoutId;
    }


    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return headerView.keyAt(position);
        }
        if (isFooterViewPos(position)) {
            return footerView.keyAt(position - getHeaderCount() - getDataCount());
        }
        return super.getItemViewType(position);
    }
    public void onCreateHeaderView(CustomViewHolder holder){

    }
    public void onCreateFooterView(CustomViewHolder holder){

    }
    public void onCreateDataView(CustomViewHolder holder){

    }
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        CustomViewHolder holder;
        if (mInflater == null) {
            mInflater = LayoutInflater.from(viewGroup.getContext());
        }
        /*添加头布局*/
        if (this.headerView != null) {
            View hView = this.headerView.get(viewType);
            if (hView != null) {
                holder = new CustomViewHolder(hView);
                setHeaderItemClick(holder);
                setHeaderItemLongClick(holder);
                onCreateHeaderView(holder);
                return holder;
            }
        }
        /*添加脚布局*/
        if (this.footerView != null) {
            View fView = this.footerView.get(viewType);
            if (fView != null) {
                holder = new CustomViewHolder(fView);
                setFooterItemClick(holder);
                setFooterItemLongClick(holder);
                onCreateFooterView(holder);
                return holder;
            }
        }
        View viewForViewType = getViewForViewType(mInflater, viewGroup, viewType);
        if(viewForViewType!=null){
            holder = new CustomViewHolder(viewForViewType);
        }else{
            View customView = getCustomView();
            if (customView != null) {
                holder = new CustomViewHolder(customView);
            } else {
                holder = new CustomViewHolder(mInflater.inflate(layoutId, viewGroup, false));
            }
        }

        setItemClickListener(holder);
        setItemLongClickListener(holder);
        onCreateDataView(holder);
        return holder;
    }
    public View getViewForViewType(LayoutInflater mInflater,ViewGroup viewGroup,int viewType){
        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            Object tag = holder.itemView.getTag(R.id.tag_layout_params);
            if(tag==null){
                return;
            }
            if (getDataCount() > 0) {
                ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) tag;
                holder.itemView.setLayoutParams(layoutParams);
            } else {
                holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
            }
            return;
        }
        int dataPosition = getDataPosition(position);
        if (mList == null || dataPosition >= mList.size()) {
            bindDataByNull(holder,dataPosition);
        } else {
            bindData(holder,dataPosition,mList.get(dataPosition));
        }
    }
    public int getDataPosition(int itemPosition){
        return itemPosition - getHeaderCount();
    }
    public int getItemPosition(int dataPosition){
        return dataPosition + getHeaderCount();
    }
    @Override
    public int getItemCount() {
        int size = getDataCount()+ getHeaderCount() + getFooterCount();
        return size;
    }

    public int getDataCount() {
        return mList == null ? 0 : mList.size();
    }


    /*****************************headerView*******************************/
    public void addHeaderView(View view) {
        addHeaderView(view, false);
    }

    public void addHeaderView(View view, boolean hideNoData) {
        if (headerView == null) {
            headerView = new SparseArrayCompat<>();
        }
        if (hideNoData) {
            view.setTag(R.id.tag_layout_params, view.getLayoutParams());
        }
        headerView.put(headerView.size() + view_type_header, view);
    }
    public void removeHeaderView(int position){
        removeHeaderView(position,false);
    }
    public void removeHeaderView(int position,boolean notify){
        if (headerView == null) {
            return;
        }
        headerView.removeAt(position);
        if(notify){
            notifyDataSetChanged();
        }
    }
    public void removeHeaderView(View view ){
        removeHeaderView(view,false);
    }

    public void removeHeaderView(View view ,boolean notify){
        if (headerView == null) {
            return;
        }
        if(headerView.containsValue(view)){
            int index = headerView.indexOfValue(view);
            removeHeaderView(index,notify);
        }
    }

    public boolean isHeaderViewPos(int position) {
        return position < getHeaderCount();
    }

    public int getHeaderCount() {
        return headerView == null ? 0 : headerView.size();
    }

    public SparseArrayCompat<View> getHeaderView() {
        if (headerView == null) {
            headerView = new SparseArrayCompat<>();
        }
        return headerView;
    }

    public void setHeaderView(SparseArrayCompat<View> headerViewList) {
        this.headerView = headerViewList;
    }

    /*****************************footerView*******************************/
    public void addFooterView(View view) {
        addFooterView(view, false);
    }

    public void addFooterView(View view, boolean hideNoData) {
        if (footerView == null) {
            footerView = new SparseArrayCompat<>();
        }
        if (hideNoData) {
            view.setTag(R.id.tag_layout_params, view.getLayoutParams());
        }
        footerView.put(footerView.size() + view_type_footer, view);
    }
    public void removeFooterView(int position){
        removeFooterView(position,false);
    }
    public void removeFooterView(int position,boolean notify){
        if(footerView==null){
            return;
        }
        footerView.removeAt(position);
        if(notify){
            notifyDataSetChanged();
        }
    }
    public void removeFooterView(View view){
        removeFooterView(view,false);
    }
    public void removeFooterView(View view,boolean notify){
        if(footerView==null){
            return;
        }
        if (footerView.containsValue(view)) {
            int index = footerView.indexOfValue(view);
            removeFooterView(index,notify);
        }
    }


    public boolean isFooterViewPos(int position) {
        if (position >= getHeaderCount() + getDataCount() && position<(getDataCount()+ getHeaderCount() + getFooterCount())) {
            return true;
        } else {
            return false;
        }
    }

    public int getFooterCount() {
        return footerView == null ? 0 : footerView.size();
    }

    public SparseArrayCompat<View> getFooterView() {
        if (footerView == null) {
            footerView = new SparseArrayCompat<>();
        }
        return footerView;
    }

    public void setFooterView(SparseArrayCompat<View> footerViewList) {
        this.footerView = footerViewList;
    }
    protected int getBottomViewCount(){
        return getFooterCount();
    }
    public void setList(List<T> list) {
        setList(list, false);
    }

    public void setList(List<T> list, boolean isNotifyData) {
        if (list == null) {
            this.mList = new ArrayList<>();
        } else {
            this.mList = list;
        }
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }

    public void addList(List<T> list) {
        addList(list, false,false);
    }

    public void addList(List<T> list, boolean isNotifyData ) {
        addList(list,isNotifyData,false);
    }
    public void addList(List<T> list, boolean isNotifyData,boolean useAnim) {
        if(list==null||list.size()<=0){
            return;
        }
        getList().addAll(list);
        if (isNotifyData) {
            if(list.size()==this.mList.size()||!useAnim){
                notifyDataSetChanged();
            }else{
                int insertPosition = mList.size() - list.size() + getHeaderCount();
                notifyItemRangeInserted(insertPosition,list.size());
                notifyItemRangeChanged(insertPosition,getItemCount()-insertPosition);
            }
        }
    }
    public void addData(T data){
        addData(data,false,false);
    }
    public void addData(T data, boolean isNotifyData,boolean useAnim) {
        if(data==null){
            return;
        }
        getList().add(data);
        if(isNotifyData){
            if(getList().size()==1||!useAnim){
                notifyDataSetChanged();
            }else{
                int insertPosition = getList().size() + getHeaderCount() - 1;
                notifyItemInserted(insertPosition);
                notifyItemRangeChanged(insertPosition,getItemCount()-insertPosition);
            }
        }
    }
    public void addData(int dataPosition,T data){
        addData(dataPosition,data,false,false);
    }
    public void addData(int dataPosition,T data, boolean isNotifyData,boolean useAnim) {
        if(data==null||dataPosition<0){
            return;
        }
        getList().add(dataPosition,data);
        if(isNotifyData){
            if(getList().size()==1||!useAnim){
                notifyDataSetChanged();
            }else{
                int itemPosition=dataPosition+getHeaderCount();
                notifyItemInserted(itemPosition);
                notifyItemRangeChanged(itemPosition,getItemCount()-itemPosition);
            }
        }
    }

    public List<T> getList() {
        return mList==null?new ArrayList<T>():mList;
    }

    public void remove(int dataPosition) {
        remove(dataPosition,false,false);
    }
    public void remove(int dataPosition, boolean isNotifyData,boolean useAnim) {
        if(mList==null||dataPosition>=mList.size()||dataPosition<0){
            return;
        }
        mList.remove(dataPosition);
        if (isNotifyData) {
            if(useAnim){
                int itemPosition=dataPosition+getHeaderCount();
                notifyItemRemoved(itemPosition);
                notifyItemRangeChanged(itemPosition,getItemCount()-itemPosition);
            }else{
                notifyDataSetChanged();
            }
        }
    }
    public void setOnItemClickListener(AdapterOnClickListener listener) {
        mClickListener = listener;
    }

    public void setOnItemLongClickListener(AdapterOnClickListener listener) {
        mLongClickListener = listener;
    }

    public void setOnHeaderClickListener(AdapterOnClickListener listener) {
        headerClickListener = listener;
    }

    public void setOnHeaderLongClickListener(AdapterOnClickListener listener) {
        headerLongClickListener = listener;
    }

    public void setOnFooterClickListener(AdapterOnClickListener listener) {
        footerClickListener = listener;
    }

    public void setOnFooterLongClickListener(AdapterOnClickListener listener) {
        footerLongClickListener = listener;
    }


    private void setHeaderItemClick(final CustomViewHolder holder) {
        if (headerClickListener != null && holder.itemView != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    headerClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                }
            });
        }
    }

    private void setHeaderItemLongClick(final CustomViewHolder holder) {
        if (headerLongClickListener != null && holder.itemView != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    headerLongClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
                    return true;
                }
            });
        }
    }

    private void setFooterItemLongClick(final CustomViewHolder holder) {
        if (footerLongClickListener != null && holder.itemView != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    footerLongClickListener.onItemClick(holder.itemView, holder.getAdapterPosition()- getHeaderCount() - getDataCount());
                    return true;
                }
            });
        }
    }

    private void setFooterItemClick(final CustomViewHolder holder) {
        if (footerClickListener != null && holder.itemView != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    footerClickListener.onItemClick(holder.itemView, holder.getAdapterPosition()- getHeaderCount() - getDataCount());
                }
            });
        }
    }

    private void setItemClickListener(final CustomViewHolder holder) {
        if (mClickListener != null && holder.itemView != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(holder.itemView, getDataPosition(holder.getAdapterPosition()));
                }
            });
        }
    }

    private void setItemLongClickListener(final CustomViewHolder holder) {
        if (mLongClickListener != null && holder.itemView != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemClick(holder.itemView, getDataPosition(holder.getAdapterPosition()));
                    return true;
                }
            });
        }
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
                    if(isHeaderViewPos(position)||isFooterViewPos(position)||needSetSpanSize(position)){
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
    protected boolean needSetSpanSize(int position) {
        return false;
    }
    protected boolean needSetFullSpan(CustomViewHolder holder) {
        return false;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CustomViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //如果不显示底部布局
        if(isHeaderViewPos(holder.getAdapterPosition())||isFooterViewPos(holder.getAdapterPosition())||needSetFullSpan(holder)){
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams sglm = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                sglm.setFullSpan(true);
            }
        }

    }
}
