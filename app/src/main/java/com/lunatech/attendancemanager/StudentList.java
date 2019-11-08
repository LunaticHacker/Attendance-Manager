package com.lunatech.attendancemanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.spec.DESedeKeySpec;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class StudentList extends AppCompatActivity implements AddStudentDialog.StudentListener,StudentAdapter.onUnCheckedListener, DatePickerDialog.OnDateSetListener,StudentAdapter.onCheckedListener {
    StudentAdapter adapter;
    Realm realm = Realm.getDefaultInstance();
    Calendar c = Calendar.getInstance();
    String dep_name;
    String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
    String pres_date =date;
    Button date_pick_button;
    Button done ;


    //RealmList<Student> studentRealmList = new RealmList<>();
    RealmList<Student> students;
    ArrayList<Student> absentStudents = new ArrayList<>();
    List<String> absentStudentsNumbers = new ArrayList<>();
    RealmList<Attendance> attendanceRealmList = new RealmList<>();

    private RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            adapter.update(students);
            adapter.notifyDataSetChanged();

        }
    };

    //Creates Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_menu,menu);
        return true;
    }
    //Check Action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.add_student: {
                openDialog();
                break;
            }
            case R.id.settings:{
                openSettings();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void openSettings() {
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.show(getSupportFragmentManager(),"Settings");
    }


    private void openDialog() {
        AddStudentDialog addStudentDialog = new AddStudentDialog();
        addStudentDialog.show(getSupportFragmentManager(),"add_student");
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        date_pick_button = findViewById(R.id.date_picker);
        done =findViewById(R.id.done_button);



         dep_name = getIntent().getExtras().getString("Class","Department Name");

        final RealmQuery<Department> query = realm.where(Department.class).equalTo("name",dep_name);

         students = query.findFirst().getStudentsList();//gets Students of the department with name which we got from Intent
         adapter = new StudentAdapter(this,students,this,this,absentStudents);

//         if(adapter.getItemCount()==0){
//             Toast.makeText(this, "Start Adding Students by Clicking the '+'", Toast.LENGTH_LONG).show();
//         }


         //Adding the Attendance on clicking the Done button
         done.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {


                 new AlertDialog.Builder(StudentList.this)
                         .setCancelable(false)
                         .setMessage("Are you Sure")
                         .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {

                                 //we loop through all students and check if their id matches the id of absent students..
                                 //in an inner_loop and sets the boolean accordingly
                                 //absent students is an array_list of students which is updated on button_click events



                                 RealmList<Student> students = query.findFirst().getStudentsList();
                                 for(Student s : students){
                                     boolean present =true;
                                     for(Student a : absentStudents){
                                         if(s!=null) {
                                             if (s.getId().equals(a.getId())) {
                                                 present = false;

                                             }
                                         }
                                     }

                                     if(present){

                                          //Toast.makeText(StudentList.this, s.getName()+" Present", Toast.LENGTH_SHORT).show();
                                         Attendance attendance = new Attendance(date+s.getId(),true,date);
                                         realm.beginTransaction();

                                         try {
                                             attendance = realm.createObject(Attendance.class,date+s.getId());
                                             attendance.setPresent(true);
                                             attendance.setDateText(date);
                                             s.getAttendanceList().add(attendance);
                                         } catch (RealmPrimaryKeyConstraintException e) {
                                             realm.copyToRealmOrUpdate(attendance);
                                         }

                                         realm.commitTransaction();

                                     }else{
                                         Attendance attendance = new Attendance(date+s.getId(),false,date);
                                         realm.beginTransaction();
                                         try {
                                             attendance = realm.createObject(Attendance.class,date+s.getId());
                                             attendance.setPresent(false);
                                             attendance.setDateText(date);
                                             s.getAttendanceList().add(attendance);

                                         } catch (RealmPrimaryKeyConstraintException e) {
                                             realm.copyToRealmOrUpdate(attendance);
                                         }
                                         realm.commitTransaction();


                                     }
                                 }
                                 Toast.makeText(StudentList.this, "Attendance Added", Toast.LENGTH_SHORT).show();
                                 absentStudentsNumbers.clear();

                                 for(Student s : absentStudents){
                                     absentStudentsNumbers.add(s.getNumber());
                                     //Log.d("R",s.getNumber());
                                 }

                                 absentStudents.clear();
                                 adapter.abupdate(absentStudents);
                                 if(absentStudentsNumbers.size()!=0&&loadData()) {
                                     sendsms(absentStudentsNumbers);
                                 }




                             }
                         }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {

                     }
                 }).show();





             }
         });
         //End of Adding the Attendance




        final RecyclerView recyclerView = findViewById(R.id.student_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        date_pick_button.setText(date);//Sets Initial Date for UX

        //Date picker Listener
        date_pick_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(),"DatePickerFragment");
            }
        });




        //Swipe to Delete Functionality
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                realm.beginTransaction();
                students.get(viewHolder.getAdapterPosition()).getAttendanceList().deleteAllFromRealm();
                students.deleteFromRealm(viewHolder.getAdapterPosition());
                realm.commitTransaction();
                Toast.makeText(StudentList.this, "Deleted the Student", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);



    }

    private void sendsms(List<String> numbers) {
        String smsto ="smsto: ";
        for(String n : numbers){
            smsto =smsto+n+";";
        }
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse(smsto));
        if(date.equals(pres_date))
            sendIntent.putExtra("sms_body", "Your Child is Absent Today ");//if the attendance was of the same day it's recorded
        else sendIntent.putExtra("sms_body", "Your Child was Absent on "+date);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            sendIntent.setPackage(Telephony.Sms.getDefaultSmsPackage(this));
        }

        if(sendIntent.getPackage()!=null)
        startActivity(sendIntent);
    }


    //This Method is from an Interface StudentListener in AddStudentDialog
    @Override
    public void ApplyText(String id, String name,String number) {
        if(id.trim().isEmpty()||name.trim().isEmpty()||number.trim().isEmpty()){
            Toast.makeText(this, "Fields Can't be Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        RealmQuery<Department> query = realm.where(Department.class).equalTo("name",dep_name);
        Department department = query.findFirst();
        realm.beginTransaction();


        Student student = new Student(dep_name+id,name,attendanceRealmList,number);

        try {
            student = realm.createObject(Student.class,dep_name+id);
            student.setName(name);
            student.setAttendanceList(attendanceRealmList);
            student.setNumber(number);
            department.getStudentsList().add(student);
        } catch (RealmPrimaryKeyConstraintException e) {
            realm.copyToRealmOrUpdate(student);


        }
        realm.commitTransaction();

    }

    @Override
    protected void onStart() {
        students.addChangeListener(realmChangeListener);
        super.onStart();
    }

    @Override
    protected void onStop() {
        students.removeAllChangeListeners();
        super.onStop();
    }

    @Override
    public void onUnchecked( Student student) {
        //uses Interface from Adapter to keep track of Unchecked boxes

        absentStudents.add(student);
        //Log.d("Debug",student.getName());
        adapter.abupdate(absentStudents);

        //Toast.makeText(this, "Absent Marked", Toast.LENGTH_SHORT).show();


    }

    //Overrides the method to set the get and set the Date from Date_picker
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,i);
        c.set(Calendar.MONTH,i1);
        c.set(Calendar.DAY_OF_MONTH,i2);
        date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        date_pick_button.setText(date);

    }

    @Override
    public void onChecked(Student student) {
        //uses Interface from Adapter to remove Students if they are checked after Unchecked
        //or Marked present after being marked Absent
        absentStudents.remove(student);
      adapter.abupdate(absentStudents);


    }
//Save AbsentStudents List for Screen Changes and similar Cases
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("absent",absentStudents);
    }
//Restores absentList and Updates Adapter
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Toast.makeText(this, "Restoring", Toast.LENGTH_SHORT).show();
        absentStudents =savedInstanceState.getParcelableArrayList("absent");
        if(absentStudents!=null) {
            adapter.abupdate(absentStudents);
        }

    }
    private boolean loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("bool",false);
    }
}
