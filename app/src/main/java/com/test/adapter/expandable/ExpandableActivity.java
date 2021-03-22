package com.test.adapter.expandable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.github.adapter.listener.AdapterOnClickListener;
import com.test.adapter.R;

import java.util.ArrayList;
import java.util.List;

public class ExpandableActivity extends AppCompatActivity {

    private RecyclerView rvExpandable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable);
        rvExpandable = findViewById(R.id.rvExpandable);
        final ExpandableAdapter adapter=new ExpandableAdapter(1);



        List<ExpandableBean> list=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ExpandableBean bean1=new ExpandableBean();
            List<ExpandableBean> list1=new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                ExpandableBean bean2=new ExpandableBean();
                List<ExpandableBean> list2=new ArrayList<>();
                for (int k = 0; k < 3; k++) {
                    ExpandableBean bean3=new ExpandableBean();
                    List<ExpandableBean> list3=new ArrayList<>();
                   /* for (int l = 0; l < 3; l++) {
                        ExpandableBean bean4=new ExpandableBean();
                        bean4.level=3;
                        bean4.title="第4级,第"+l+"个item";
                        list3.add(bean4);
                    }*/
//                    bean3.list=list3;
                    bean3.level=2;
                    bean3.title="第3级,第"+k+"个item";
                    list2.add(bean3);
                }
                bean2.list=list2;
                bean2.level=1;
                bean2.title="第2级,第"+j+"个item";
                list1.add(bean2);
            }
            bean1.list=list1;
            bean1.level=0;
            bean1.title="第1级,第"+i+"个item";
            list.add(bean1);
        }

        test(list,1);

        adapter.setList(list);
//        adapter.expandAll();
        List<ExpandableBean> list1 = adapter.getList();

        rvExpandable.setLayoutManager(new LinearLayoutManager(this));
        rvExpandable.setAdapter(adapter);


        adapter.setOnItemClickListener(new AdapterOnClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if(adapter.isExpand(position)){
                    adapter.collapseItem(position,true,false);
                }else{
                    adapter.expandItem(position,true,false);
                }
            }
        });
    }

    public static void test(List<ExpandableBean> list,int num){
        if(list==null){
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ExpandableBean expandableBean = list.get(i);
            StringBuilder sb=new StringBuilder();
            for (int j = 0; j < num; j++) {
                sb.append("    ");
            }
            Log.i("=====",""+sb+""+expandableBean.getLevel()+"==test==="+expandableBean.title);
            test(expandableBean.getChildList(),num+1);
        }
    }
}
