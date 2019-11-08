package com.lunatech.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;

public class AttendanceListActivity extends AppCompatActivity {

    RealmList<Attendance> attendanceRealmList;
    RecyclerView recyclerView;
    AttendanceListAdapter adapter;
    Realm realm = Realm.getDefaultInstance();
    String student_id;
    TextView textView;
    int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        student_id= getIntent().getExtras().getString("student_id","student_id");
        final RealmQuery<Student> query  = realm.where(Student.class).equalTo("id",student_id);
        //uses the value passed from Intent to get the Attendance List of that Student
        attendanceRealmList = query.findFirst().getAttendanceList();
        textView = findViewById(R.id.percentage);

        adapter = new AttendanceListAdapter(this,attendanceRealmList,student_id);
        recyclerView = findViewById(R.id.attendance_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        for(Attendance attendance : attendanceRealmList){
            if(attendance.isPresent()){
                p++;//p is for number of presents for calculating percentage
            }
        }
        if(attendanceRealmList.size()!=0) {
            String percentage = (p * 100) / attendanceRealmList.size()+"% Attendance";//finding percentage of Attendance of Student
            textView.setText(percentage);
        }else{
            Toast.makeText(this, "No Attendance Ever Added", Toast.LENGTH_SHORT).show();
        }

    }


}
//No ChangeListeners are used here since we are not letting the user to make changes in this Activity
//This is the Activity that can have a lot more features