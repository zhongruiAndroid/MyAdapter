package com.github.adapter;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/15.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder{
    protected SparseArray<View> mViews;//集合类，layout里包含的View,以view的id作为key，value是view对象
    protected Context mContext;//上下文对象
    public RecyclerViewHolder(Context ctx, View itemView) {
        super(itemView);
        mContext = ctx;
        mViews = new SparseArray<View>();
    }
    protected <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }
    public <V extends View> V getViewByType(Class<V> a, int viewId) {
        return (V) getView(viewId);
    }

    public  <T extends View> T getView(int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    public ImageButton getImageButton(int viewId) {
        return (ImageButton) getView(viewId);
    }

    public EditText getEditText(int viewId) {
        return (EditText) getView(viewId);
    }

    public RecyclerViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public RecyclerViewHolder setText(int viewId, CharSequence value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }
    public RecyclerViewHolder setText(int viewId, CharSequence value, TextView.BufferType bufferType) {
        TextView view = findViewById(viewId);
        view.setText(value,bufferType);
        return this;
    }
    public RecyclerViewHolder setText(int viewId,@StringRes int value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }
    public RecyclerViewHolder setText(int viewId, @StringRes int value, TextView.BufferType bufferType) {
        TextView view = findViewById(viewId);
        view.setText(value,bufferType);
        return this;
    }
    public RecyclerViewHolder setText(int viewId, char[] value, int start,int len) {
        TextView view = findViewById(viewId);
        view.setText(value,start,len);
        return this;
    }
    public RecyclerViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public RecyclerViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
