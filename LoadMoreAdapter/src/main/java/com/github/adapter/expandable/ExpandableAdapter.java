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
    public View getViewForViewType(LayoutInflater mInflater, ViewGroup viewGroup, int viewType) {
        int layoutId = getLayoutId(viewType);
        if (layoutId != VIEW_TYPE_NONE) {
            return mInflater.inflate(layoutId, viewGroup, false);
        } else {
            return getLayoutView(viewType);
        }
    }

    /*展开所有item*/
    public void expandAll() {
        expandAll(false, false);
    }

    public void expandAll(boolean needNotify, boolean useAnim) {
        List<T> list = getList();
       /* for (int i = list.size()-1; i >=0; i--) {
            expandAll(i,false,false);
        }*/
        int count = 0;
        /*因为要遍历下一个，所以count要加1*/
        for (int i = 0; i < getList().size(); i = i + count + 1) {
            count = expandItem(i, needNotify, useAnim);
            Log.i("=====", i + "===expandAll==" + count);
        }
    }

    private int expandItem(int dataPosition, boolean needNotify, boolean useAnim) {
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
        /*因为要遍历下一个，所以count要加1*/
        for (int i =0; i < childList.size(); i++) {
            int i1 = i + dataPosition + 1 + count;
            count = expandItem(i+dataPosition+1+count, needNotify, useAnim);
            Log.i("=====",i1 + "===expandAll2==" + count);
            childSize += count;
        }
        if (needNotify) {
            if (useAnim) {
                notifyItemRangeChanged(getItemPosition(dataPosition), getItemPosition(dataPosition) + childSize);
            } else {
                notifyDataSetChanged();
            }
        }
        return childSize;
    }

    /*展开某个item里面的所有子item以及子子XXitem*/
    public int expandAll(int dataPosition, boolean needNotify, boolean useAnim) {
        IExpandable needBreakItem;
        if (dataPosition + 1 < getList().size()) {
            needBreakItem = getExpandableItem(dataPosition + 1);
        }
        IExpandable item = getExpandableItem(dataPosition);
        if (item == null) {
            return 0;
        }
        /*如果需要展开的item没有下级*/
        if (item.getChildList() == null || item.getChildList().size() == 0) {
            item.setExpandable(true);
            if (needNotify) {
                if (useAnim) {
                    notifyItemChanged(getItemPosition(dataPosition));
                } else {
                    notifyDataSetChanged();
                }
            }
            return 0;
        }
        /*将postion下的item添加到list中*/
        int expandCount = expand(dataPosition, false, false);
        return expandCount;
    }

    /*获取某个item下面的子item*/
    private int expand(@IntRange(from = 0) int dataPosition, boolean needNotify, boolean useAnim) {
        IExpandable item = getExpandableItem(dataPosition);
        if (item == null) {
            return 0;
        }
        /*如果需要展开的item没有下级*/
        if (item.getChildList() == null || item.getChildList().size() == 0) {
            item.setExpandable(true);
            if (needNotify) {
                if (useAnim) {
                    notifyItemChanged(getItemPosition(dataPosition));
                } else {
                    notifyDataSetChanged();
                }
            }
            return 0;
        }
        int childCount = 0;
        /*如果当前item没有展开*/
        if (!item.isExpandable()) {
            List childList = item.getChildList();
        }
        if (needNotify) {
            if (useAnim) {
                int itemPosition = getItemPosition(dataPosition);
                notifyItemRangeChanged(itemPosition, getItemCount() - itemPosition);
            } else {
                notifyDataSetChanged();
            }
        }
        return childCount;
    }

    private IExpandable getExpandableItem(int dataPosition) {
        IExpandable iExpandable = getList().get(dataPosition);
        return iExpandable;
    }

}
