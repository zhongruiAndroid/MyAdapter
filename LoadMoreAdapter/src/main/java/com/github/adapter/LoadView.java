package com.github.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


public class LoadView {

    private final String TAG_LOAD_VIEW = "TAG_LOAD_VIEW";
    private final String TAG_NO_MORE_VIEW = "TAG_NO_MORE_VIEW";
    private final String TAG_ERROR_VIEW = "TAG_ERROR_VIEW";

    private String loadViewText = "正在加载更多...";
    private String noMoreViewText = "暂无更多";
    private String errorViewText = "加载失败,点击重试";
    private int loadViewHeight = 0;
    private int bottomViewBackground = Color.TRANSPARENT;

    public View getLoadView() {
        return null;
    }

    public View getErrorView() {
        return null;
    }

    public View getNoMoreView() {
        return null;
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

    private int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale);
    }

    public FrameLayout getLayout(Context context, int currentStatus) {
        FrameLayout loadLayout = new FrameLayout(context);
        int loadViewHeight = getLoadViewHeight();
        if (loadViewHeight <= 0) {
            loadViewHeight = dp2px(context, 40);
        }
        loadLayout.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, loadViewHeight));
        /*显示加载更多*/
        View loadView;
        if (getLoadView() != null) {
            loadView = getLoadView();
        } else {
            loadView = getDefaultView(context, getLoadViewText());
        }
        loadView.setTag(TAG_LOAD_VIEW);

        /*暂无更多数据*/
        View noMoreView;
        if (getNoMoreView() != null) {
            noMoreView = getLoadView();
        } else {
            noMoreView = getDefaultView(context, getNoMoreViewText());
        }
        noMoreView.setTag(TAG_NO_MORE_VIEW);

        /*加载失败*/
        View errorView;
        if (getErrorView() != null) {
            errorView = getLoadView();
        } else {
            errorView = getDefaultView(context, getErrorViewText());
        }
        errorView.setTag(TAG_ERROR_VIEW);


        switch (currentStatus) {
            case LoadMoreAdapter.status_load:
                loadView.setVisibility(View.VISIBLE);
                noMoreView.setVisibility(View.INVISIBLE);
                errorView.setVisibility(View.INVISIBLE);
                break;
            case LoadMoreAdapter.status_no_more:
                loadView.setVisibility(View.INVISIBLE);
                noMoreView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.INVISIBLE);
                break;
            case LoadMoreAdapter.status_error:
                loadView.setVisibility(View.INVISIBLE);
                noMoreView.setVisibility(View.INVISIBLE);
                errorView.setVisibility(View.VISIBLE);
                break;
        }
        loadLayout.addView(loadView);
        loadLayout.addView(noMoreView);
        loadLayout.addView(errorView);
        return loadLayout;
    }

    public void changeLoadView(ViewGroup loadLayout, int currentStatus) {
        switch (currentStatus) {
            case LoadMoreAdapter.status_load:
                changeViewByTag(loadLayout, TAG_LOAD_VIEW, true);
                changeViewByTag(loadLayout, TAG_NO_MORE_VIEW, false);
                changeViewByTag(loadLayout, TAG_ERROR_VIEW, false);
                break;
            case LoadMoreAdapter.status_no_more:
                changeViewByTag(loadLayout, TAG_LOAD_VIEW, false);
                changeViewByTag(loadLayout, TAG_NO_MORE_VIEW, true);
                changeViewByTag(loadLayout, TAG_ERROR_VIEW, false);
                break;
            case LoadMoreAdapter.status_error:
                changeViewByTag(loadLayout, TAG_LOAD_VIEW, false);
                changeViewByTag(loadLayout, TAG_NO_MORE_VIEW, false);
                changeViewByTag(loadLayout, TAG_ERROR_VIEW, true);
                break;
        }
    }

    private void changeViewByTag(ViewGroup loadLayout, String tag, boolean show) {
        if (loadLayout == null) {
            return;
        }
        View viewWithTag = loadLayout.findViewWithTag(tag);
        if (viewWithTag != null) {
            viewWithTag.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private TextView getDefaultView(Context context, String text) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        int loadViewHeight = getLoadViewHeight();
        if (loadViewHeight <= 0) {
            loadViewHeight = dp2px(context, 40);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, loadViewHeight);
        textView.setLayoutParams(layoutParams);
        layoutParams.gravity = Gravity.CENTER;
        textView.setBackgroundColor(bottomViewBackground);
        textView.setText(text);
        return textView;
    }
}
