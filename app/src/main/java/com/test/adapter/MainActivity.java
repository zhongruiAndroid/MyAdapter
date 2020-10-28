package com.test.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.adapter.CustomAdapter;
import com.github.adapter.CustomViewHolder;
import com.github.adapter.LoadInter;
import com.github.adapter.LoadMoreAdapter;
import com.test.adapter.dividerline.BaseItemDivider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.rv);
        MyAdapter myAdapter = new MyAdapter<String>(this, R.layout.test_item, 15) {
            @Override
            public void bindData(CustomViewHolder holder, int position, String bean) {
                Log.i("===bindData", "==========" + position);
                holder.setText(R.id.tv, bean);
                TextView textView = holder.getTextView(R.id.tv);
                Random random = new Random();
                int i = random.nextInt(160) + 130;

//                textView.setHeight(i);
                textView.setHeight(288);
            }
        };
        TextView textView = new TextView(this);
        textView.setText("Android!头布局1");
        textView.setPadding(0, 50, 0, 50);
        textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setBackgroundColor(Color.parseColor("#87E6D1"));


        TextView textView2 = new TextView(this);
        textView2.setText("IOS!jió布局");
        textView2.setPadding(0, 50, 0, 50);
        textView2.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView2.setBackgroundColor(Color.parseColor("#87E6D1"));

        myAdapter.addHeaderView(textView);

        textView = new TextView(this);
        textView.setText("Android!2头布局2");
        textView.setPadding(0, 50, 0, 50);
        textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setBackgroundColor(Color.parseColor("#87E6D1"));
        myAdapter.addHeaderView(textView);

        myAdapter.addFooterView(textView2);

        myAdapter.setOnLoadMoreListener(new LoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void loadMore(LoadInter loadInter) {

            }
        });
        myAdapter.setOnFooterClickListener(new CustomAdapter.OnFooterClickListener() {
            @Override
            public void onFooterClick(View itemView, int position) {

            }
        });

        View bannerView = LayoutInflater.from(this).inflate(R.layout.test_item, (ViewGroup) recyclerView.getParent(), false);

//        myAdapter.addHeaderView(bannerView);


        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("content" + i);
        }
        myAdapter.setList(list);
        BaseItemDivider baseItemDivider = new BaseItemDivider(this, 20, ContextCompat.getColor(this, R.color.c_divider));
        baseItemDivider.setSkipStartCount(2);
        baseItemDivider.setSkipEndCount(2);
//        baseItemDivider.setHeaderCount(2);
//        baseItemDivider.addSkipPosition(8);
//        baseItemDivider.setShowLastLine(true);
        recyclerView.addItemDecoration(baseItemDivider);
//        recyclerView.addItemDecoration(new BaseDividerGridItem(this,50),0);

//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }
}
