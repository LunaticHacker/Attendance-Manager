package com.lunatech.attendancemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.RealmList;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.myViewHolder> {
    Context mContext;
    RealmList<Attendance> attendanceRealmList;
    String id;


    public AttendanceListAdapter(Context mContext, RealmList<Attendance> attendanceRealmList,String id ){
        this.mContext = mContext;
        this.attendanceRealmList = attendanceRealmList;
        this.id =id;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =(LayoutInflater.from(mContext).inflate(R.layout.attendance_list,parent,false));
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        if(attendanceRealmList.size()!=0) {
            String present;
            Attendance attendance =attendanceRealmList.get(position);
            if(attendance.isPresent()){
                present = " Present";
            }else{
                present =" Absent";
            }

            String date = attendance.getDateText();

            holder.textView.setText(date+present);
        }


    }

    @Override
    public int getItemCount() {
        return attendanceRealmList.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.attendance_list_text);

        }
    }
}
