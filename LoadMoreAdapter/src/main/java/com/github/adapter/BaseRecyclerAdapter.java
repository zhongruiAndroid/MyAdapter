package com.github.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {

    protected List<T> mList;
    protected final Context mContext;
    protected LayoutInflater mInflater;
    private OnItemClickListener mClickListener;
    private OnItemLongClickListener mLongClickListener;
    private int layoutId;
    /**假数据测试设置list大小**/
    protected int testListSize=0;
    abstract public void bindData(RecyclerViewHolder holder, int position, T item);

    public BaseRecyclerAdapter(Context ctx, int layoutId) {
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
        this.layoutId=layoutId;
    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerViewHolder holder = new RecyclerViewHolder(mContext,
                mInflater.inflate(this.layoutId, parent, false));
        if (mClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickListener.onItemLongClick(holder.itemView, holder.getLayoutPosition());
                    return true;
                }
            });
        }
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if(mList==null||position>=mList.size()){
            bindData(holder, position,null);
        }else{
            bindData(holder, position,testListSize>0?null:mList.get(position));
        }
    }

    public void setTestListSize(int testListSize) {
        this.testListSize = testListSize;
    }

    @Override
    public int getItemCount() {
        if(testListSize>0){
            return testListSize;
        }else{
            return mList ==null?0: mList.size();
        }
    }

    public void setList(List<T> list) {
        setList(list, false);
    }
    public void setList(List<T> list, boolean isNotifyData) {
        if(list==null){
            this.mList=new ArrayList<>();
        }else{
            this.mList = list;
        }
        if (isNotifyData) {
            notifyDataSetChanged();
        }
    }
    public void addList(List<T> list) {
        addList(list, false);
    }
    public void addList(List<T> list, boolean isNotifyData) {
        if(this.mList==null){
            this.mList=new ArrayList<>();
        }
        if(list!=null){
            this.mList.addAll(list);
            if (isNotifyData) {
                notifyDataSetChanged();
            }
        }
    }
    public List<T> getList() {
        return mList;
    }
    public void delete(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }
    public void deleteNotifyData(int position,boolean isNotifyData) {
        mList.remove(position);
        if(isNotifyData){
            notifyDataSetChanged();
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongClickListener = listener;
    }
    public interface OnItemClickListener {
        public void onItemClick(View itemView, int position);
    }
    public interface OnItemLongClickListener {
        public void onItemLongClick(View itemView, int position);
    }
}
