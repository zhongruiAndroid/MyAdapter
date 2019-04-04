package com.test.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.github.adapter.MyLoadMoreAdapter;
import com.github.adapter.MyRecyclerViewHolder;


public abstract class MyAdapter<T> extends MyLoadMoreAdapter<T> {
    public MyAdapter(Context mContext, int layoutId, int pageSize) {
        super(mContext, layoutId, pageSize);
    }
    public MyAdapter(Context mContext, int layoutId, int pageSize, NestedScrollView nestedScrollView) {
        super(mContext, layoutId, pageSize, nestedScrollView);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyRecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

        if(layoutParams!=null&&layoutParams instanceof StaggeredGridLayoutManager.LayoutParams){
            if(holder.getAdapterPosition()==0||holder.getAdapterPosition()==getItemCount()-1||holder.getAdapterPosition()==getItemCount()-2){
//                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager!=null&&layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager= (GridLayoutManager) layoutManager;
//            final  GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(position==0||position==getItemCount()-1||position==getItemCount()-2){
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }
}
