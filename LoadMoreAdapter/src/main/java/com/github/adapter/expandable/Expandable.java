package com.github.adapter.expandable;

import java.util.List;

public interface Expandable<T> {
    List<T> getChildList();
    boolean isExpandable();
    void setExpandable(boolean expandable);
    int getLevel();
    void setLevel(int level);
}
