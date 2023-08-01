package com.example.simple_bookkeeping;

import android.app.Application;

import com.example.simple_bookkeeping.db.DBManager;

/* 表示全局的应用的类 */
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库对象
        DBManager.initDB(getApplicationContext());
    }
}
