package com.github.adapter.expandable;

import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import com.github.adapter.LoadMoreAdapter;

import java.util.List;

public abstract class ExpandableAdapter extends LoadMoreAdapter<IExpandable> {
    public static final int VIEW_TYPE_NONE = -1;
    private SparseIntArray expandableLayoutId;
    private SparseArray<View> expandableLayoutView;

    public ExpandableAdapter(int layoutId) {
        super(layoutId);
    }

    private int getLayoutId(int viewType) {
        return expandableLayoutId.get(viewType, VIEW_TYPE_NONE);
    }
    private View getLayoutView(int viewType) {
        return expandableLayoutView.get(viewType, null);
    }
    protected void addItemViewType(int type, @LayoutRes int layoutResId) {
        if (expandableLayoutId == null) {
            expandableLayoutId = new SparseIntArray();
        }
        expandableLayoutId.put(type, layoutResId);
    }
    protected void addItemViewType(int type, View view) {
        if (expandableLayoutView == null) {
            expandableLayoutView = new SparseArray();
        }
        expandableLayoutView.put(type, view);
    }

    public void expandAll() {
        List<IExpandable> list = getList();
        for (int i = list.size()-1; i >=0; i--) {
            expand(i,false,false);
        }
    }

    public void expand(int dataPosition,boolean useAnim,boolean needNotify) {
        IExpandable expandableItem = getExpandableItem(dataPosition);


    }
    private IExpandable getExpandableItem(int dataPosition){
        IExpandable iExpandable = getList().get(dataPosition);
        return iExpandable;
    }

}
