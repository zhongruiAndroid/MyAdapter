package com.github.adapter;

public interface LoadListener {
    void loadHasMore(boolean hasMore);
    void loadHasMore(boolean hasMore,boolean hiddenNoMoreView);
    void loadError();
}
