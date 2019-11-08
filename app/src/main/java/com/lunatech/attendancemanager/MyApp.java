package com.lunatech.attendancemanager;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //RealmConfigurations
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("db.realm").build();
        Realm.setDefaultConfiguration(config);
    }
}
