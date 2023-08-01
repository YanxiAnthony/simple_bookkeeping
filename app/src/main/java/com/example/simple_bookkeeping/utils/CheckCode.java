package com.example.simple_bookkeeping.utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.simple_bookkeeping.R;
import com.example.simple_bookkeeping.SettingActivity;

public class CheckCode extends Dialog implements View.OnClickListener {
    public static final String TAG = CheckCode.class.getName();
    private ImageView iv_showCode;
    private EditText et_inputCode;
    private Button toSetCode;
    //产生的验证码
    private String realCode;

    public CheckCode(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);
        toSetCode = findViewById(R.id.btn_toSetCodes);
        iv_showCode = findViewById(R.id.iv_showCode);
        et_inputCode = findViewById(R.id.et_inputCodes);
        //将验证码用图片的形式显示出来
        iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
        iv_showCode.setOnClickListener(this);
        toSetCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_showCode:
                iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                Log.v(TAG, "realCode" + realCode);
                break;
            case R.id.btn_toSetCodes:
                initEvent();
                break;
        }
    }

    //判断验证码是否相等
    private void initEvent() {
        toSetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputCode = et_inputCode.getText().toString().toLowerCase();
                if (!inputCode.equals(realCode)) {  //验证码不正确
                    Toast.makeText(getContext(), "验证码不正确！", Toast.LENGTH_SHORT).show();
                    System.out.println(realCode + "   " + inputCode);
                    iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                    realCode = Code.getInstance().getCode().toLowerCase();
                    Log.v(TAG, "realCode  " + realCode);
                    et_inputCode.setText(null);
                } else if (inputCode.equals(realCode)) {
                    cancel();
                }
            }
        });
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
        wlp.gravity = (int) (d.getHeight());
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}