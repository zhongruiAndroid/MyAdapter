package com.test.adapter.expandable;

import com.github.adapter.expandable.Expandable;

import java.util.List;

public class ExpandableBean implements Expandable<ExpandableBean> {
    private boolean expandable;
    private int level;
    public String title;
    public List<ExpandableBean> list;

    public List<ExpandableBean> getChildList() {
        return list;
    }

    @Override
    public boolean isExpandable() {
        return expandable;
    }

    @Override
    public int getLevel() {
        return level;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setChildList(List<ExpandableBean> list) {
        this.list = list;
    }
}
