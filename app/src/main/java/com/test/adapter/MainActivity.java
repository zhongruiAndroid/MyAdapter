package com.test.adapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.adapter.MyLoadMoreAdapter;
import com.github.adapter.MyRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView=findViewById(R.id.rv);
        MyAdapter myAdapter = new MyAdapter<String>(this, R.layout.test_item, 15) {
            @Override
            public void bindData(MyRecyclerViewHolder holder, int position, String bean) {
                Log.i("===bindData","=========="+position);
                holder.setText(R.id.tv,bean);
            }
        };
        myAdapter.setOnLoadMoreListener(new MyLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void loadMore() {

            }
        });


        View bannerView = LayoutInflater.from(this).inflate(R.layout.test_item, (ViewGroup) recyclerView.getParent(), false);

        myAdapter.addHeaderView(bannerView);


        List<String> list=new ArrayList<>();
        list.add("1");
        list.add("2");
//        myAdapter.setList(list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }
}
