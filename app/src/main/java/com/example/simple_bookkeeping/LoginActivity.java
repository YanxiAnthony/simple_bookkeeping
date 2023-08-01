package com.example.simple_bookkeeping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simple_bookkeeping.db.MyDBOpenHelper;

public class LoginActivity extends AppCompatActivity {
    private MyDBOpenHelper mDBOpenHelper;
    private Button loginBtn;
    private TextView signupTv;
    private EditText username, password;
    private CheckBox pwdRemenber, AutoLogin;
    public String userNameValue, passwordValue;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDBOpenHelper = new MyDBOpenHelper(this);
        initView();
        initEvent();
        spEvent();
//        sp = getSharedPreferences("userInfo", 0);
//        if(sp.getBoolean("autologin", true)){
//            Login();
//        }
    }

    @Override
    /*接受传回来的账号*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        username.setText(bundle.getString("aaa"));
    }

    /*初始化控件*/
    private void initView() {
        username = findViewById(R.id.login_et_username);
        password = findViewById(R.id.login_et_password);
        loginBtn = findViewById(R.id.login_btn_login);
        signupTv = findViewById(R.id.login_tv_signup);
        pwdRemenber = findViewById(R.id.login_cb_pwdRemenber);
        AutoLogin = findViewById(R.id.login_cb_autoLogin);
    }

    /*页面跳转判断*/
    private void initEvent() {
        /*跳转注册页面*/
        signupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSignupActivity();
            }
        });
        /*判断条件，跳转主页面*/
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    /*登录判断实现类*/
    private void Login() {
        String name = username.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {  //判断账号、密码是否都不为空
            if (mDBOpenHelper.queryCountByAccount(name) != 0) {   //账号是否存在
                if (TextUtils.equals(pwd, mDBOpenHelper.queryKeyByAcount(name))) {  //输入的密码与数据库中账号对应的密码是否相等
                    Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
                    userNameValue = username.getText().toString();
                    passwordValue = password.getText().toString();
                    SharedPreferences.Editor editor = sp.edit();
                    if (TextUtils.equals(pwd, mDBOpenHelper.queryKeyByAcount(name))) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show(); //保存用户名和密码
                        editor.putString("USER_NAME", userNameValue);
                        editor.putString("PASSWORD", passwordValue); //是否记住密码
                        if (pwdRemenber.isChecked()) {
                            editor.putBoolean("remember", true);
                        } else {
                            editor.putBoolean("remember", false);
                        } //是否自动登录
                        if (AutoLogin.isChecked()) {
                            editor.putBoolean("autologin", true);
                        } else {
                            editor.putBoolean("autologin", false);
                        }
                        editor.commit();
                        toMainActivity();
                        finish();//销毁此Activity
                    } else {
                        Toast.makeText(this, "账号或密码不正确", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, "账号不存在", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_LONG).show();
        }
    }

    /*记住密码自动登录*/
    private void spEvent() {
        sp = getSharedPreferences("userInfo", 0);
        String name = sp.getString("USER_NAME", "");
        String pass = sp.getString("PASSWORD", "");
        boolean choseRemember = sp.getBoolean("remember", true);
        boolean choseAutoLogin = sp.getBoolean("autologin", true);
        if (choseRemember) {
            username.setText(name);
            password.setText(pass);
            pwdRemenber.setChecked(true);
        } //如果上次登录选了自动登录，那进入登录页面也自动勾选自动登录
        if (choseAutoLogin) {
            AutoLogin.setChecked(true);
        }
    }

    /*跳转至MainActivity*/
    public void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /*跳转至SignupActivity*/
    public void toSignupActivity() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, SignupActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);//将Bundle添加到Intent,也可以在Bundle中添加相应数据传递给下个页面,例如：bundle.putString("abc", "bbb");
        startActivityForResult(intent, 0);// 跳转并要求返回值，0代表请求值(可以随便写)
    }
}