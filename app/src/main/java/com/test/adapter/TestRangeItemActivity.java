package com.test.adapter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.github.adapter.CustomAdapter;
import com.github.adapter.CustomViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TestRangeItemActivity extends AppCompatActivity {

    private RecyclerView rvTestRangeItem;
    private CustomAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_range_item);

        Button btAdd = findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getList().set(1,"set");
                adapter.notifyItemChanged(1);
//                adapter.notifyItemInserted(1);
//                adapter.notifyItemRangeChanged(1,adapter.getItemCount()-1);
            }
        });

        Button btRemove = findViewById(R.id.btRemove);
        btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.getList().set(1,"set2");
                adapter.notifyDataSetChanged();
//                adapter.getList().remove(1);
//                adapter.notifyItemRemoved(1);
//                adapter.notifyItemRangeChanged(1,adapter.getItemCount()-1);
            }
        });
        rvTestRangeItem = findViewById(R.id.rvTestRangeItem);

        rvTestRangeItem.setLayoutManager(new LinearLayoutManager(this));


        List<String> list=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i+"");
        }
        adapter = new CustomAdapter(android.R.layout.simple_list_item_1) {
            @Override
            public void bindData(CustomViewHolder holder, int position, Object item) {
                holder.setText(android.R.id.text1, item + "+++" + position + "" + position + "" + position + "" + position + "" + position + "" + position);
            }
        };
        adapter.setList(list);
        rvTestRangeItem.setAdapter(adapter);


    }
}
