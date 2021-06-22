package com.test.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.github.adapter.CustomViewHolder;
import com.github.adapter.LoadMoreAdapter;


public abstract class MyAdapter<T> extends LoadMoreAdapter<T> {
    Context mContext;
    public MyAdapter(Context mContext, int layoutId, int pageSize) {
        super( layoutId  );
        this.mContext=mContext;
    }
    @Override
    public void onViewAttachedToWindow(@NonNull CustomViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

        if(layoutParams!=null&&layoutParams instanceof StaggeredGridLayoutManager.LayoutParams){
            if(holder.getAdapterPosition()==0||holder.getAdapterPosition()==getItemCount()-1||holder.getAdapterPosition()==getItemCount()-2){
//                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            }
        }
    }

   /* @Override
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
    }*/
}
