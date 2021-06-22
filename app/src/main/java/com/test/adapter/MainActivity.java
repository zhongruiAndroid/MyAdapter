package com.test.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.adapter.CustomViewHolder;
import com.github.adapter.LoadListener;
import com.github.adapter.LoadMoreAdapter;
import com.github.adapter.listener.AdapterOnClickListener;
import com.test.adapter.dividerline.BaseItemDivider;
import com.test.adapter.expandable.ExpandableActivity;
import com.test.adapter.test.TestAdapterActivity;
import com.test.adapter.testload.TestLoadActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View btTestAdapter = findViewById(R.id.btTestAdapter);
        btTestAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestAdapterActivity.class));
            }
        });
        View btTestLoadAdapter = findViewById(R.id.btTestLoadAdapter);
        btTestLoadAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestLoadActivity.class));
            }
        });
        View btTestRangeItem = findViewById(R.id.btTestRangeItem);
        btTestRangeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestRangeItemActivity.class));
            }
        });
        View btTestExpandable = findViewById(R.id.btTestExpandable);
        btTestExpandable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ExpandableActivity.class));
            }
        });
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
            public void loadMore(LoadListener loadListener) {
            }
        });
        myAdapter.setOnFooterClickListener(new AdapterOnClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

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
