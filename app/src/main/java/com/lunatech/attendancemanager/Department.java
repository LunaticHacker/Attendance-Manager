package com.lunatech.attendancemanager;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Department extends RealmObject {

    @PrimaryKey
    private  String name;
    private RealmList<Student> studentsList;

    public Department() {
    }

    public Department (String name, RealmList<Student> studentsList) {

        this.name = name;
        this.studentsList = studentsList;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Student> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(RealmList<Student> studentsList) {
        this.studentsList = studentsList;
    }
}
