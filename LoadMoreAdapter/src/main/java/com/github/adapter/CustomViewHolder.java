package com.github.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/***
 *   created by zhongrui on 2019/3/25
 */
public class CustomViewHolder extends RecyclerView.ViewHolder {
    private SparseArrayCompat<View> sparseView;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        sparseView = new SparseArrayCompat<>();
    }
    public <T extends View> T getView(int viewId) {
        View view = sparseView.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            sparseView.put(viewId, view);
        }
        return (T) view;
    }

    public ImageView getImageView(int viewId) {
        return getView(viewId);
    }

    public TextView getTextView(int viewId) {
        return getView(viewId);
    }

    /******************************TextView************************************/
    public CustomViewHolder setText(int viewId, String value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public CustomViewHolder setText(int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public CustomViewHolder setText(int viewId, CharSequence value, TextView.BufferType bufferType) {
        TextView view = getView(viewId);
        view.setText(value, bufferType);
        return this;
    }

    public CustomViewHolder setText(int viewId, @StringRes int value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public CustomViewHolder setText(int viewId, @StringRes int value, TextView.BufferType bufferType) {
        TextView view = getView(viewId);
        view.setText(value, bufferType);
        return this;
    }

    public CustomViewHolder setText(int viewId, char[] value, int start, int len) {
        TextView view = getView(viewId);
        view.setText(value, start, len);
        return this;
    }
}
