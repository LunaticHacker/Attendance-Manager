package com.lunatech.attendancemanager;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.Button;

import android.widget.TextView;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.realm.RealmList;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.mViewHolder> {

   private Context mContext;
   private RealmList<Student> realmResults;
   private onUnCheckedListener onUnCheckedListener;
   private onCheckedListener onCheckedListener;
   private  List<Student> absentStudents;

    public StudentAdapter(Context mContext, RealmList realmResults, onUnCheckedListener unCheckedListener, onCheckedListener onCheckedListener,List<Student> absentStudents) {
        this.mContext = mContext;
        this.realmResults = realmResults;
        this.onUnCheckedListener = unCheckedListener;
        this.onCheckedListener = onCheckedListener;
        this.absentStudents = absentStudents;
    }

    //Update the ItemList from RealmChangeListener
    void update(RealmList realmResults){
        this.realmResults = realmResults;
    }
    void abupdate(List absentStudents){
        this.absentStudents = absentStudents;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view =(LayoutInflater.from(mContext).inflate(R.layout.student_list_recycler_view,parent,false));
         return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final mViewHolder holder, final int position) {

            final Student student = realmResults.get(position);
            holder.student_name.setText(student.getName());
            String id = student.getId();
            //clean up the id for ux using regex
            String clean_id =id.replaceAll("[^\\d]", "");
            holder.student_id.setText(clean_id);
            holder.checkBox.setText(R.string.present);


            for(Student s :this.absentStudents){
               // Log.d("R",s.getName());

                if(student.getId().equals(s.getId())){
                    holder.checkBox.setText(R.string.absent);
                    return;

                }

            }


            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(absentStudents.contains(student)){
                        onCheckedListener.onChecked(student);
                        holder.checkBox.setText(R.string.present);
                    }else{
                        onUnCheckedListener.onUnchecked(student);
                        holder.checkBox.setText(R.string.absent);
                    }
                }
            });
            holder.student_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext,AttendanceListActivity.class);
                    intent.putExtra("student_id",student.getId());//passing the StudentID for further queries
                    mContext.startActivity(intent);
                }
            });






    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    public  class mViewHolder  extends RecyclerView.ViewHolder{

        TextView student_name;
        TextView student_id;
        Button checkBox;


        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            this.student_id = itemView.findViewById(R.id.student_id_recycler_view);
            this.student_name = itemView.findViewById(R.id.student_name_recycler_view);
            this.checkBox = itemView.findViewById(R.id.checkbox);


        }
    }

    public interface onUnCheckedListener{
        void onUnchecked(Student student);
    }
    public interface onCheckedListener{
        void onChecked(Student student);
    }
}
