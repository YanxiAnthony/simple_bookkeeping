package com.example.simple_bookkeeping.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.simple_bookkeeping.AboutActivity;
import com.example.simple_bookkeeping.HistoryActivity;
import com.example.simple_bookkeeping.LoginActivity;
import com.example.simple_bookkeeping.MonthChartActivity;
import com.example.simple_bookkeeping.R;
import com.example.simple_bookkeeping.SettingActivity;
import com.example.simple_bookkeeping.toexcel.ToExcelActivity;

public class MoreDialog extends Dialog implements View.OnClickListener {
    Button aboutBtn, settingBtn, historyBtn, infoBtn, signOut, toExcelBtn;
    ImageView errorIv;
    private SharedPreferences sp;

    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);
        aboutBtn = findViewById(R.id.dialog_more_btn_about);
        settingBtn = findViewById(R.id.dialog_more_btn_setting);
        historyBtn = findViewById(R.id.dialog_more_btn_record);
        infoBtn = findViewById(R.id.dialog_more_btn_info);
        errorIv = findViewById(R.id.dialog_more_iv);
        signOut = findViewById(R.id.dialog_more_btn_signout);
        toExcelBtn = findViewById(R.id.dialog_more_btn_toExcel);
        //设置点击事件
        aboutBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        historyBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        errorIv.setOnClickListener(this);
        signOut.setOnClickListener(this);
        toExcelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.dialog_more_btn_about:
                intent.setClass(getContext(), AboutActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_setting:
                intent.setClass(getContext(), SettingActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_record:
                intent.setClass(getContext(), HistoryActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_info:
                intent.setClass(getContext(), MonthChartActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_btn_signout:
                intent.setClass(getContext(), LoginActivity.class);
                getContext().startActivity(intent);
//                SharedPreferences.Editor editor = sp.edit();
//                editor.putBoolean("autologin", false);
//                editor.commit();
                break;
            case R.id.dialog_more_btn_toExcel:
                intent.setClass(getContext(), ToExcelActivity.class);
                getContext().startActivity(intent);
                break;
            case R.id.dialog_more_iv:
                break;
        }
        cancel();
    }

    /* 设置Dialog的尺寸和屏幕尺寸一致*/
    public void setDialogSize() {
        //获取当前窗口对象
        Window window = getWindow();
        //获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        //获取屏幕宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int) (d.getWidth());  //对话框窗口为屏幕窗口
        wlp.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}