package com.github.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/5/15.
 */

public class MyRecyclerViewHolder extends RecyclerViewHolder{
    BottomView bottomView;
    public MyRecyclerViewHolder(Context ctx, View itemView) {
        super(ctx,itemView);
        mContext = ctx;
        mViews = new SparseArray<View>();
        if(itemView instanceof BottomView){
            bottomView= (BottomView) itemView;
        }
    }
    public static class BottomView extends LinearLayout {
        public BottomView(Context context) {
            super(context);
        }
        public BottomView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }
        public BottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
    }
}
