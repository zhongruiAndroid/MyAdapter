package com.test.adapter.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.test.adapter.R;

import java.util.ArrayList;
import java.util.List;

public class TestAdapterActivity extends AppCompatActivity {
    RecyclerView rvTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_adapter);

        rvTest = findViewById(R.id.rvTest);
        rvTest.setLayoutManager(new LinearLayoutManager(this));
        TestAdapter adapter=new TestAdapter();
        List<String> list=new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            list.add(i+"");
        }
        adapter.setList(list);
        rvTest.setAdapter(adapter);
//        rvTest.getItemAnimator().setChangeDuration(0);
    }
}
