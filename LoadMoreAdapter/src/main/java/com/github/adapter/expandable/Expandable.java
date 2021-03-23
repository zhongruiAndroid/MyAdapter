package com.github.adapter.expandable;

import java.util.List;

public abstract class Expandable<T> {
    private boolean expandable;
    private int level;

    public abstract List<T> getChildList();

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
