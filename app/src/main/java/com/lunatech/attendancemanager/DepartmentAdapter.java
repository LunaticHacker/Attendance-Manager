package com.lunatech.attendancemanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import io.realm.RealmResults;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.myViewHolder> {
    private Context mContext;
    private RealmResults<Department> realmList;

    public DepartmentAdapter(Context mContext, RealmResults<Department> realmList) {
        this.mContext = mContext;
        this.realmList = realmList;
       // update(realmList);
    }

    public void update(RealmResults<Department> departments){
        realmList = departments;
    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.department_recycler_view,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {
        holder.textView .setText(realmList.get(position).getName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, realmList.get(position).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,StudentList.class);
                intent.putExtra("Class",realmList.get(position).getName());
                //passes the department name for further queries
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return realmList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
    LinearLayout linearLayout;
    TextView textView;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        this.linearLayout = itemView.findViewById(R.id.dep_linear);
        this.textView = itemView.findViewById(R.id.dep_name_text_view);
    }
}
}

