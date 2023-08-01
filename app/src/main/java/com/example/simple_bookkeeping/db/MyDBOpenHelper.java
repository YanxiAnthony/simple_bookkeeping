package com.example.simple_bookkeeping.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBOpenHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db; //声明一个数据库变量db
    private static final String DB_NAME="mySQLite.db";
    private static final String TABLE_NAME="user";

    //简化后，仅需传入contex（上下文）一个参数即可创建MyOpenHelper对象
    public MyDBOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
        db=getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库SQL语句
        String CREATE_TABLE_SQL= "create table "+TABLE_NAME+" (id Integer primary key autoincrement,account text,login_key text,name text)";
        //创建数据库表
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //更新需做的操作
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }

    //插入用户
    public void insert(String account,String loginKey,String name){
        String INSERT_USER_SQL="insert into "+TABLE_NAME+" (account,login_key,name) values(?,?,?)";
        db.execSQL(INSERT_USER_SQL,new Object[]{account,loginKey,name});

    }

    //根据账号查找密码
    public String queryKeyByAcount(String account){
        String QUERY_KEY_BYACOUNT_SQL="select login_key from "+TABLE_NAME+" where account=?";
        //rawQuery()⽅法的第⼀个参数为select语句；第⼆个参数为select语句中占位符参数的值，如果select语句没有使⽤占位符，该参数可以设置为null。
        Cursor cursor=db.rawQuery(QUERY_KEY_BYACOUNT_SQL,new String[]{account});
        //因为账号是唯一的，所以查找的结果有且只有一条，直接将游标的指向为first即可
        cursor.moveToFirst();
        String loginKey=cursor.getString(0);
        //关闭游标
        cursor.close();
        return loginKey;
    }

    //在数据库中查找账号是否已经存在
    public Integer queryCountByAccount(String account){
        Cursor cursor=db.rawQuery("select count(*) from "+TABLE_NAME+" where account=?",new String[]{account});
        cursor.moveToFirst();
        int count=cursor.getInt(0);
        cursor.close();
        return count;
    }
}
