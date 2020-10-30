package com.test.adapter.testload;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.adapter.CustomViewHolder;
import com.github.adapter.LoadListener;
import com.github.adapter.LoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

public class TestLoadAdapter extends LoadMoreAdapter<String> implements LoadMoreAdapter.OnLoadMoreListener {
    Context context;
    public TestLoadAdapter(Context layoutId) {
        super(android.R.layout.simple_list_item_1);
        context=layoutId;
        setOnLoadMoreListener(this);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.i("=====","=====CustomViewHolder");
        return super.onCreateViewHolder(viewGroup, viewType);
    }

    @Override
    public void bindData(CustomViewHolder holder, int position, String item) {
        Log.i("=====", "=====" + item);
        holder.setText(android.R.id.text1, item);
    }
    private Handler handler;
    private Handler getHandler() {
        if (handler == null) {
            handler=new Handler(Looper.getMainLooper());
        }
        return handler;
    }
    int count=1;
    @Override
    public void loadMore(final LoadListener loadListener) {
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(count==5){
                    addList(getTestList("第"+count+"组item",3),true);
                    loadListener.loadHasMore(false,true);
                    return;
                }
                if(count%2==0){
                    loadListener.loadError();

//                    addList(getTestList("第"+count+"组item",3),true);
//                    loadListener.loadHasMore(true);
                }else{
                    addList(getTestList("第"+count+"组item",3),true);
                    loadListener.loadHasMore(true);
                }
                count+=1;
            }
        },1000);
    }

    public View getLoadView() {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(context, 40));
        textView.setLayoutParams(layoutParams);
        textView.setText("load");
        return textView;
    }
    private int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale);
    }
    public List getTestList(String des, int count) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(des + i);
        }
        return list;
    }
}
