package com.test.adapter.testload;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.adapter.R;
import com.test.adapter.dividerline.BaseItemDivider;

import java.util.ArrayList;
import java.util.List;

public class TestLoadActivity extends AppCompatActivity {
    RecyclerView rvTestLoad;
    TestLoadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_load);
        initView();
    }

    private void initView() {
        rvTestLoad = findViewById(R.id.rvTestLoad);
        BaseItemDivider baseItemDivider = new BaseItemDivider(this, 30, Color.RED);
        baseItemDivider.setSkipStartCount(2);
        baseItemDivider.setSkipEndCount(3);
        rvTestLoad.addItemDecoration(baseItemDivider);
//        rvTestLoad.setLayoutManager(new LinearLayoutManager(this));
        rvTestLoad.setLayoutManager(new GridLayoutManager(this,3));

        adapter = new TestLoadAdapter(this);
        TextView textView = new TextView(this);
        textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));
        textView.setText("header--android");
        adapter.addHeaderView(textView);

        textView = new TextView(this);
        textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));
        textView.setText("header--java");
        adapter.addHeaderView(textView);


        textView = new TextView(this);
        textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));
        textView.setText("FooterView--ios");
        adapter.addFooterView(textView);


        textView = new TextView(this);
        textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));
        textView.setText("FooterView--kotlin");
        adapter.addFooterView(textView);


        adapter.setList(adapter.getTestList("第1组item", 15));
        adapter.loadHasMore(true);

        rvTestLoad.setAdapter(adapter);
    }

}
