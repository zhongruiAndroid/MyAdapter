package com.github.adapter.expandable;

import java.util.List;

public interface IExpandable<T> {
    List<T> getChildList();
    boolean isExpandable();
    void setExpandable(boolean expandable);
    int getLevel();
}
