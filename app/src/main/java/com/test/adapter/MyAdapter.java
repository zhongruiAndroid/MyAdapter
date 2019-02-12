package com.test.adapter;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;

import com.github.adapter.MyLoadMoreAdapter;


public abstract class MyAdapter<T> extends MyLoadMoreAdapter<T> {
    public MyAdapter(Context mContext, int layoutId, int pageSize) {
        super(mContext, layoutId, pageSize);
    }
    public MyAdapter(Context mContext, int layoutId, int pageSize, NestedScrollView nestedScrollView) {
        super(mContext, layoutId, pageSize, nestedScrollView);
    }
}
