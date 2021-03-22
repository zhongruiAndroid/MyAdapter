package com.github.adapter.expandable;

import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpandableAdapter<T extends IExpandable> extends LoadMoreAdapter<T> {
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

    public void addItemViewType(int type, @LayoutRes int layoutResId) {
        if (expandableLayoutId == null) {
            expandableLayoutId = new SparseIntArray();
        }
        expandableLayoutId.put(type, layoutResId);
    }

    public void addItemViewType(int type, View view) {
        if (expandableLayoutView == null) {
            expandableLayoutView = new SparseArray();
        }
        expandableLayoutView.put(type, view);
    }

    @Override
    public void setList(List<T> list, boolean isNotifyData) {
       /* if(list!=null){
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setExpandable(true);
            }
        }*/
        super.setList(list, isNotifyData);
    }

    @Override
    public View getViewForViewType(LayoutInflater mInflater, ViewGroup viewGroup, int viewType) {
        int layoutId = getLayoutId(viewType);
        if (layoutId != VIEW_TYPE_NONE) {
            return mInflater.inflate(layoutId, viewGroup, false);
        } else {
            return getLayoutView(viewType);
        }
    }
    public boolean isExpand(int dataPosition){
        if(dataPosition<0||dataPosition>=getList().size()){
            return false;
        }
        T t = getList().get(dataPosition);
        return t.isExpandable();
    }
    public boolean isCollapse(int dataPosition){
        return !isExpand(dataPosition);
    }
    /*展开所有item*/
    public void expandAll() {
        expandAll(false, false);
    }

    public void expandAll(boolean needNotify, boolean useAnim) {
        int count = 0;
        int startSize = getList().size();
        int dataPosition = 0;
        for (int i = 0; i < startSize; i++) {
            count = expandItem(dataPosition, needNotify, useAnim);
            dataPosition = count + 1 + dataPosition;
        }
    }

    public int expandItem(int dataPosition, boolean needNotify, boolean useAnim) {
        IExpandable item = getExpandableItem(dataPosition);
        if (item == null || item.isExpandable()) {
            return 0;
        }
        item.setExpandable(true);
        if (item.getChildList() == null || item.getChildList().size() == 0) {
            if (needNotify) {
                if (useAnim) {
                    notifyItemChanged(getItemPosition(dataPosition));
                } else {
                    notifyDataSetChanged();
                }
            }
            return 0;
        }
        List childList = item.getChildList();

        int childSize = childList.size();
        getList().addAll(dataPosition + 1, childList);

        int count = 0;


        int startSize = childList.size();
        for (int i = 0; i < startSize; i++) {
            dataPosition = count + 1 + dataPosition;
            count = expandItem(dataPosition, needNotify, useAnim);
            childSize += count;
        }
        if (needNotify) {
            if (useAnim) {
                notifyItemRangeChanged(getItemPosition(dataPosition), 1 + childSize);
            } else {
                notifyDataSetChanged();
            }
        }
        return childSize;
    }

    /*关闭所有item*/
    public void collapseAll() {
        collapseAll(false, false);
    }

    public void collapseAll(boolean needNotify, boolean useAnim) {
        if (getList() == null) {
            return;
        }
        List<T> list = new ArrayList<>();
        for (int i = 0; i < getList().size(); i++) {
            T t = getList().get(i);
            if (t == null) {
                continue;
            }
            int level = t.getLevel();
            if (level == 0) {
                list.add(t);
            }
        }
        setList(list);
        if (needNotify) {
            if (useAnim) {
                notifyItemRangeChanged(getItemPosition(0), getItemCount() - getItemPosition(0));
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public int collapseItem(int dataPosition, boolean needNotify, boolean useAnim) {
        if (dataPosition < 0) {
            return 0;
        }
        IExpandable expandable = getExpandableItem(dataPosition);
        if (expandable == null) {
            return 0;
        }
        if (!expandable.isExpandable()) {
            return 0;
        }
        expandable.setExpandable(false);
        int level = expandable.getLevel();
        List<Integer> indexList = new ArrayList<>();
        for (int i = dataPosition + 1; i < getList().size(); i++) {
            IExpandable item = getExpandableItem(i);
            if (item != null && item.getLevel() <= level) {
                break;
            }
            if(item!=null){
                item.setExpandable(false);
            }
            indexList.add(i);
        }
        for (int i = indexList.size() - 1; i >= 0; i--) {
            int integer = indexList.get(i);
            getList().remove(integer);
        }
        if (needNotify) {
            if (useAnim) {
                int itemPosition = getItemPosition(dataPosition);
                notifyItemRangeChanged(itemPosition, getItemCount() - itemPosition);
            } else {
                notifyDataSetChanged();
            }
        }
        return indexList.size();
    }


    private IExpandable getExpandableItem(int dataPosition) {
        /*if(getList()==null){
            return null;
        }
        if(dataPosition>=getList().size()){
            return null;
        }*/
        IExpandable iExpandable = getList().get(dataPosition);
        return iExpandable;

    }

}
