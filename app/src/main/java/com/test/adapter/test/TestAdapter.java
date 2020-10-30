package com.test.adapter.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.test.adapter.R;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private List<String> list=new ArrayList<>();
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TestViewHolder testViewHolder = new TestViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_adapter_item, viewGroup, false), this);
        Log.i("=====","=====onCreateViewHolder"+testViewHolder.getAdapterPosition());
        return testViewHolder;
    }
    public void setList(List<String> list) {
        this.list = list;
    }
    @Override
    public void onBindViewHolder(@NonNull final TestViewHolder holder, final int position) {
        Log.i("=====","=====onBindViewHolder:"+position);
        holder.tv.setText("position:"+position+"\ndata:"+getList().get(position));
        holder.btNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v.getContext(),position+"="+holder.getAdapterPosition());
//                TestAdapter.this.notifyDataSetChanged();
            }
        });
        holder.btAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList().add(position,"newData"+position);
//                getList().add(position+1,"newData"+position);
//                getList().add(position+2,"newData"+position);
//                notifyDataSetChanged();
                notifyItemRangeInserted(position,1);
//                notifyItemRangeChanged(position,getList().size());
           /*     holder.itemView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemRangeChanged(position,getList().size());
                    }
                },1000);*/
            }
        });
        holder.btDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList().remove(position);
                getList().remove(position);
                notifyItemRangeRemoved(position,2);
                notifyItemRangeChanged(position,getList().size()-position);
            }
        });
        holder.btUpdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList().set(position,"Update"+position);
                notifyItemChanged(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        Log.i("=====","=====getItemCount"+(list==null?0:list.size()));
        return list==null?0:list.size();
    }

    public List<String> getList() {
        return list;
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        Button btAdd;
        Button btDelete;
        Button btUpdate;
        Button btAdd2;
        Button btDelete2;
        Button btUpdate2;
        Button btNotify;
        TestAdapter adapter;
        public TestViewHolder(@NonNull final View itemView, final TestAdapter ad) {
            super(itemView);
            adapter=ad;
            tv=itemView.findViewById(R.id.tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("=====","=====");
                    showDialog(tv.getContext(),"getAdapterPosition"+getAdapterPosition()+"\n"+"getLayoutPosition:"+getLayoutPosition()+"\n"+"data:"+adapter.getList().get(getAdapterPosition()));
                }
            });
            btAdd=itemView.findViewById(R.id.btAdd);
            btAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.getList().add(getAdapterPosition(),"newData"+getAdapterPosition());
                    adapter.notifyItemInserted(getAdapterPosition());
                }
            });
            btDelete=itemView.findViewById(R.id.btDelete);
            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.getList().remove(getAdapterPosition());
                    adapter.notifyItemRemoved(getAdapterPosition());
                }
            });

            btUpdate=itemView.findViewById(R.id.btUpdate);
            btUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.getList().set(getAdapterPosition(),"Update"+getAdapterPosition());
                    adapter.notifyItemChanged(getAdapterPosition());
                }
            });

            btAdd2=itemView.findViewById(R.id.btAdd2);
            btDelete2=itemView.findViewById(R.id.btDelete2);
            btUpdate2=itemView.findViewById(R.id.btUpdate2);

            btNotify=itemView.findViewById(R.id.btNotify);
            btNotify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.notifyDataSetChanged();
                }
            });

        }

        private void showDialog(Context context,String string){
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setMessage(string);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }
    private void showDialog(Context context,String string){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage(string);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
