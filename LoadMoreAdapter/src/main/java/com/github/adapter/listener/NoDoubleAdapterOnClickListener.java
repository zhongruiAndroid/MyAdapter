//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.adapter.listener;

import android.view.View;

import java.util.Calendar;

public abstract class NoDoubleAdapterOnClickListener implements AdapterOnClickListener {
    private static long MIN_CLICK_DELAY_TIME = 900;
    private long lastClickTime;
    private View preView;
    public NoDoubleAdapterOnClickListener() {
        this(900);
    }

    public NoDoubleAdapterOnClickListener(long interval) {
        this.lastClickTime = 0L;
        MIN_CLICK_DELAY_TIME = interval;
    }

    public abstract void onNoDoubleClick(View itemView, int position);

    @Override
    public void onItemClick(View itemView, int position) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - this.lastClickTime > MIN_CLICK_DELAY_TIME||preView!=itemView) {
            this.preView = itemView;
            this.lastClickTime = currentTime;
            this.onNoDoubleClick(itemView, position);
        }
    }
}
