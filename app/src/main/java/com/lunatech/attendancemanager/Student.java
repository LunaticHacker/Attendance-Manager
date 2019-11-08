package com.lunatech.attendancemanager;



import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Student extends RealmObject implements Parcelable {

    @PrimaryKey private String  id;
    private String  name;
    private RealmList<Attendance> attendanceList;
    private String number;

    public Student() {
    }

    public Student(String id, String name, RealmList<Attendance> attendanceList,String number) {
        this.id = id;
        this.name = name;
        this.attendanceList = attendanceList;
        this.number = number;


    }

    protected Student(Parcel in) {
        id = in.readString();
        name = in.readString();
        number = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String  name) {
        this.name = name;
    }

    public RealmList<Attendance> getAttendanceList() {
        return attendanceList;
    }

    public void setAttendanceList(RealmList<Attendance> attendanceList) {
        this.attendanceList = attendanceList;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(number);
    }
}
