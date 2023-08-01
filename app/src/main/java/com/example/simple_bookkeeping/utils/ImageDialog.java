package com.example.simple_bookkeeping.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.simple_bookkeeping.LoginActivity;
import com.example.simple_bookkeeping.MainActivity;
import com.example.simple_bookkeeping.R;


public class ImageDialog extends Dialog implements View.OnClickListener {
    TextView album, camera, cancel;

    public ImageDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_image);
        album = findViewById(R.id.dialog_image_tv_album);
        camera = findViewById(R.id.dialog_image_tv_camera);
        cancel = findViewById(R.id.dialog_image_tv_cancel);
        album.setOnClickListener(this);
        camera.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.dialog_image_tv_album:

                break;
            case R.id.dialog_image_tv_camera:

                break;
            default:
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