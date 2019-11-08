package com.lunatech.attendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmResults;


//I didn't follow a pattern/Architecture to build this app..which may make certain parts of the code look like garbage
//but hey All CODE IS GARBAGE And I will Refactor that Later :)

public class MainActivity extends AppCompatActivity implements DepartmentDialog.DepDialogListener {
    RecyclerView recyclerView;
    DepartmentAdapter adapter;
    RealmResults<Department> realmResults;
    private static final Pattern p =Pattern.compile("[\\d]");
    final Realm realm = Realm.getDefaultInstance();


    //Listener for RealmResults
    private RealmChangeListener realmChangeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            adapter.update(realmResults);
            adapter.notifyDataSetChanged();

        }
    };


//Creates Option Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
//Check Action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.add: {
                openDialog();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final Realm realm = Realm.getDefaultInstance();

        realmResults = realm.where(Department.class).findAllAsync();//Query
        adapter = new DepartmentAdapter(this,realmResults);


        //Setting up the RecyclerView

        recyclerView = findViewById(R.id.dep_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //if(adapter.getItemCount()==0){
        //    Toast.makeText(this, "Start Adding Departments by clicking on the '+'", Toast.LENGTH_LONG).show();
       // }





        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                realm.beginTransaction();
                for(Student s : realmResults.get(viewHolder.getAdapterPosition()).getStudentsList()){
                    s.getAttendanceList().deleteAllFromRealm();
                }

                realmResults.get(viewHolder.getAdapterPosition()).getStudentsList().deleteAllFromRealm();
                realmResults.deleteFromRealm(viewHolder.getAdapterPosition());
                realm.commitTransaction();
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();


            }
        }).attachToRecyclerView(recyclerView);



    }
//Writes to Database by getting data from Realm using DialogBox
    @Override
    public void ApplyText(String dep_name) {
        if(dep_name.trim().isEmpty()){
            Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show();
            return;
        }
        Matcher m = p.matcher(dep_name);
        if(m.find())
        {
            Toast.makeText(this, "Cannot use numbers in department name", Toast.LENGTH_LONG).show();
            return;

        }
       // Realm realm = Realm.getDefaultInstance();
        RealmList<Student> studentRealmList = new RealmList<>();
        final Department department = new Department(dep_name,studentRealmList);

       realm.executeTransactionAsync(new Realm.Transaction() {
           @Override
           public void execute(Realm realm) {
               realm.copyToRealmOrUpdate(department);
           }
       }, new Realm.Transaction.OnSuccess() {
           @Override
           public void onSuccess() {
               Toast.makeText(MainActivity.this, "Data Added", Toast.LENGTH_SHORT).show();

           }
       }, new Realm.Transaction.OnError() {
           @Override
           public void onError(Throwable error) {
               Toast.makeText(MainActivity.this, "Error Adding Data", Toast.LENGTH_SHORT).show();
           }
       });


    }

    void openDialog(){
        DepartmentDialog departmentDialog = new DepartmentDialog();
        departmentDialog.show(getSupportFragmentManager(),"dep_dialog");
    }

    @Override
    protected void onStart() {
        realmResults.addChangeListener(realmChangeListener);
        super.onStart();
    }

    @Override
    protected void onStop() {
        realmResults.removeAllChangeListeners();
        super.onStop();
    }
}
