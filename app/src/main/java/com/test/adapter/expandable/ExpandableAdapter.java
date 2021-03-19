package com.test.adapter.expandable;

import android.view.View;

import com.github.adapter.CustomViewHolder;
import com.test.adapter.R;

public class ExpandableAdapter extends com.github.adapter.expandable.ExpandableAdapter<ExpandableBean> {

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;
    public static final int TYPE_LEVEL_3 = 3;

    public ExpandableAdapter(int layoutId) {
        super(layoutId);
        addItemViewType(TYPE_LEVEL_0, R.layout.item_expandable1);
        addItemViewType(TYPE_LEVEL_1, R.layout.item_expandable2);
        addItemViewType(TYPE_LEVEL_2, R.layout.item_expandable3);
        addItemViewType(TYPE_LEVEL_3, R.layout.item_expandable4);
        setCloseLoadMore(true);
    }

    @Override
    public void bindData(CustomViewHolder holder, int position, ExpandableBean item) {
        View view;
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                holder.setText(R.id.tvItem1, item.title+(item.isExpandable()?"↓":"→")+position);
                view = holder.getView(R.id.tvItem1);
                view.setPadding(item.getLevel() * 60, 0, 0, 0);
                break;
            case TYPE_LEVEL_1:
                holder.setText(R.id.tvItem2, item.title+(item.isExpandable()?"↓":"→")+position);
                view = holder.getView(R.id.tvItem2);
                view.setPadding(item.getLevel() * 60, 0, 0, 0);
                break;
            case TYPE_LEVEL_2:
                holder.setText(R.id.tvItem3, item.title+(item.isExpandable()?"↓":"→")+position);
                view = holder.getView(R.id.tvItem3);
                view.setPadding(item.getLevel() * 60, 0, 0, 0);
                break;
            case TYPE_LEVEL_3:
                holder.setText(R.id.tvItem4, item.title+(item.isExpandable()?"↓":"→")+position);
                view = holder.getView(R.id.tvItem4);
                view.setPadding(item.getLevel() * 60, 0, 0, 0);
                break;
        }
    }
}
