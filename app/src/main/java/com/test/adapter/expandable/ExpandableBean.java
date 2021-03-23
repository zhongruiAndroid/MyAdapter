package com.test.adapter.expandable;

import com.github.adapter.expandable.Expandable;

import java.util.List;

public class ExpandableBean extends Expandable<ExpandableBean> {
    public String title;
    public List<ExpandableBean> list;

    public List<ExpandableBean> getChildList() {
        return list;
    }
}
