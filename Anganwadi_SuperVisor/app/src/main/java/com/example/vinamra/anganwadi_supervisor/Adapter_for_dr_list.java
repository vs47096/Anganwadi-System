package com.example.vinamra.anganwadi_supervisor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter_for_dr_list extends RecyclerView.Adapter<Adapter_for_dr_list.Model>{
 List<class_for_dr_list> list;
 Context context;

    public Adapter_for_dr_list(List<class_for_dr_list> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Model onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_for_dr_list, parent, false);
        return new Model(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Model holder, int position) {
        class_for_dr_list obj=list.get(position);
        holder.childdata.setText(obj.getChilddata());
        holder.name.setText(obj.getName());
        holder.date.setText(obj.date);
        holder.org.setText(obj.getOrg());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Model extends RecyclerView.ViewHolder{
        TextView name,org,date,childdata;
        public Model(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.name);
            org= (TextView) itemView.findViewById(R.id.org);
            date= (TextView) itemView.findViewById(R.id.date);
            childdata= (TextView) itemView.findViewById(R.id.childdata);

        }
    }
}
