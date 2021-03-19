package com.test.adapter.expandable;

import com.github.adapter.expandable.IExpandable;

import java.util.List;

public class ExpandableBean implements IExpandable<ExpandableBean> {
    public String title;
    public List<ExpandableBean> list;
    private boolean expandable;
    public int level;
    @Override
    public List<ExpandableBean> getChildList() {
        return list;
    }

    @Override
    public boolean isExpandable() {
        return expandable;
    }

    @Override
    public void setExpandable(boolean expandable) {
        this.expandable=expandable;
    }
    @Override
    public int getLevel() {
        return level;
    }
}
