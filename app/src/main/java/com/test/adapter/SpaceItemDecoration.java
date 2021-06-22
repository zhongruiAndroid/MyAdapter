package com.test.adapter;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space=50;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
//        if (parent.getChildLayoutPosition(view) % 2 == 0) {
//            outRect.right = space;
//        }
    }

}