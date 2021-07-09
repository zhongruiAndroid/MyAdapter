package com.github.adapter.expandable;

import androidx.annotation.LayoutRes;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class ExpandableAdapter<T extends Expandable> extends LoadMoreAdapter<T> {
    public static final int VIEW_TYPE_NONE = -1;
    private SparseIntArray expandableLayoutId;
    private SparseArray<View> expandableLayoutView;

    public ExpandableAdapter(int layoutId) {
        super(layoutId);
    }

    private int getLayoutId(int viewType) {
        if (expandableLayoutId == null) {
            expandableLayoutId = new SparseIntArray();
        }
        return expandableLayoutId.get(viewType, VIEW_TYPE_NONE);
    }

    private View getLayoutView(int viewType) {
        if (expandableLayoutView == null) {
            expandableLayoutView = new SparseArray();
        }
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

    public boolean isExpand(int dataPosition) {
        if (dataPosition < 0 || dataPosition >= getList().size()) {
            return false;
        }
        T t = getList().get(dataPosition);
        return t.isExpandable();
    }

    public boolean isCollapse(int dataPosition) {
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
            /*计算完再根据参数，判断是否更新数据和设置动画*/
            count = expandItem(dataPosition, true, false, false);
            dataPosition = count + 1 + dataPosition;
        }
        if (needNotify) {
            if (useAnim) {
                int itemPosition = getItemPosition(0);
                notifyItemRangeChanged(itemPosition, getItemCount() - itemPosition);
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public int expandItem(int dataPosition, boolean needNotify, boolean useAnim) {
        return expandItem(dataPosition, false, needNotify, useAnim);
    }

    public int expandItem(int dataPosition, boolean expandChildItem, boolean needNotify, boolean useAnim) {
        Expandable item = getExpandableItem(dataPosition);
        if (item == null) {
            return 0;
        }
        if (item.isExpandable()) {
            return 0;
        }
        /*首先把当前item设置为展开状态*/
        item.setExpandable(true);
        int count = recursiveExpandItem(item, dataPosition, expandChildItem);

        if (needNotify) {
            if (useAnim) {
                int itemPosition = getItemPosition(dataPosition);
                notifyItemRangeChanged(itemPosition, getItemCount() - itemPosition);
            } else {
                notifyDataSetChanged();
            }
        }
        return count;
    }

    private int recursiveExpandItem(Expandable item, int dataPosition, boolean expandChildItem) {
        if (item == null) {
            return 0;
        }
        if (item.getChildList() == null || item.getChildList().size() == 0) {
            return 0;
        }
        List childList = item.getChildList();

        int childSize = childList.size();
        getList().addAll(dataPosition + 1, childList);
        if(childSize>0){
            notifyItemRangeInserted(getItemPosition(dataPosition+1),childSize);
        }
        int count = 0;
        int startSize = childList.size();
        for (int i = 0; i < startSize; i++) {
            dataPosition = count + 1 + dataPosition;
            Expandable expandableItem = getExpandableItem(dataPosition);
            /*如果需要展开所有子item或者当前item是展开状态*/
            if (expandChildItem) {
                expandableItem.setExpandable(true);
            }
            if (expandableItem.isExpandable()) {
                count = recursiveExpandItem(expandableItem, dataPosition, expandChildItem);
            } else {
                /*如果不展开某个item的子item,统计子item展开数量时，不能继续使用上一个item展开的子item数量，得清零*/
                count = 0;
            }
            childSize += count;
        }
        return childSize;
    }

    /*关闭所有item*/
    public void collapseAll() {
        collapseAll(false);
    }

    public void collapseAll(boolean needNotify ) {
        collapseAll(false,needNotify);
    }

    @Deprecated
    public void collapseAll(boolean collapseAllChildItem, boolean needNotify) {
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
                if(!t.isExpandable()){
                    /*如果当前菜单已经是关闭的，但是子item有可能有展开状态的，需要遍历所有*/
                    collapseCheckAllChildItem(t);
                }
                t.setExpandable(false);
                list.add(t);
            } else if (collapseAllChildItem) {
                /*如果只是将第一级菜单折叠，则只需要在当前list里面取数据改变状态，如果需要将所有二级及以下菜单折叠，需要遍历所有数据*/
                if(!t.isExpandable()){
                    collapseCheckAllChildItem(t);
                }
                t.setExpandable(false);
            }
        }
        setList(list);
        if (needNotify) {
            notifyDataSetChanged();
        }
    }
    private void collapseCheckAllChildItem(T item){
        if(item==null){
            return;
        }
        item.setExpandable(false);
        List<T> childList = item.getChildList();
        if(childList==null){
            return;
        }
        for(T child:childList){
            collapseCheckAllChildItem(child);
        }
    }
    public int collapseItem(int dataPosition, boolean needNotify, boolean useAnim) {
        return collapseItem(dataPosition, false, needNotify, useAnim);
    }

    public int collapseItem(int dataPosition, boolean collapseChildItem, boolean needNotify, boolean useAnim) {
        if (dataPosition < 0) {
            return 0;
        }
        Expandable expandable = getExpandableItem(dataPosition);
        if (expandable == null || !expandable.isExpandable()) {
            return 0;
        }
        expandable.setExpandable(false);
        int level = expandable.getLevel();
        List<Integer> indexList = new ArrayList<>();
        for (int i = dataPosition + 1; i < getList().size(); i++) {
            Expandable item = getExpandableItem(i);
            if (item != null && item.getLevel() <= level) {
                break;
            }
            /*如果要折叠某个item的所有子item*/
            if (item != null && collapseChildItem) {
                item.setExpandable(false);
            }
            indexList.add(i);
        }
        for (int i = indexList.size() - 1; i >= 0; i--) {
            int position = indexList.get(i);
            getList().remove(position);
            notifyItemRemoved(getItemPosition(position));
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
    public int getParentDataPosition(int childDataPosition){
        T t = getList().get(childDataPosition);
        if(t==null){
            return -1;
        }
        int level = t.getLevel();
        if(level==0){
            return childDataPosition;
        }else if(level<0){
            return -1;
        }
        return getParentDataPosition(t);
    }
    public int getParentDataPosition(T childData){
        if(childData==null){
            return -1;
        }
        int level = childData.getLevel();
        int position = getList().indexOf(childData);
        if(level==0){
            return position;
        }else if(level<0){
            return -1;
        }
        for (int i = position; i >=0; i--) {
            T t = getList().get(i);
            if(t==null){
                continue;
            }
            if(t.getLevel()<level&&t.getLevel()>=0){
                return i;
            }
        }
        return -1;
    }
    private Expandable getExpandableItem(int dataPosition) {
        /*if(getList()==null){
            return null;
        }
        if(dataPosition>=getList().size()){
            return null;
        }*/
        Expandable iExpandable = getList().get(dataPosition);
        return iExpandable;

    }

}
