package com.example.simple_bookkeeping;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simple_bookkeeping.db.MyDBOpenHelper;
import com.example.simple_bookkeeping.utils.BeiZhuDialog;
import com.example.simple_bookkeeping.utils.BudgetDialog;
import com.example.simple_bookkeeping.utils.CheckCode;
import com.example.simple_bookkeeping.utils.ImageDialog;
import com.example.simple_bookkeeping.utils.MoreDialog;

import java.util.TimerTask;
import java.util.regex.Pattern;

/*注册*/
public class SignupActivity extends AppCompatActivity {
    private MyDBOpenHelper mDBOpenHelper;
    private Button signupbtn;
    private TextView signupTv, fileTv;
    private EditText names, username, password, passwordComfirm;
    //读取照片
    private static final int CHOOSE_PHOTO = 2;
    private ImageView signupImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mDBOpenHelper = new MyDBOpenHelper(this);
        initView();
        initEvent();
        //点击添加头像
        signupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileTv.setVisibility(View.GONE);
                if (ContextCompat.checkSelfPermission(SignupActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SignupActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
    }

    private void initEvent() {
        //返回登录界面
        signupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //执行注册操作
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = names.getText().toString().trim();
                String uname = username.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                String pwdCom = passwordComfirm.getText().toString().trim();
                signup(name, uname, pwd, pwdCom);
            }
        });
/*        //点击添加头像
        signupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageDialog imageDialog = new ImageDialog(SignupActivity.this);
                imageDialog.show();
                imageDialog.setDialogSize();
                Toast.makeText(SignupActivity.this, "点击了头像", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void initView() {
        names = findViewById(R.id.signup_et_name);
        signupbtn = findViewById(R.id.signup_btn_signup);
        username = findViewById(R.id.signup_et_username);
        password = findViewById(R.id.signup_et_password);
        passwordComfirm = findViewById(R.id.signup_et_comfirmpassword);
        signupTv = findViewById(R.id.signup_tv_toLogin);
        signupImg = findViewById(R.id.signup_img_file);
        fileTv = findViewById(R.id.signup_tv_file);
    }

    /*注册验证*/
    private void signup(String name, String uname, String pwd, String pwdCom) {
        if (!TextUtils.isEmpty(uname) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(pwdCom)) {
            if (isMobile(uname)) {  //判断手机号是否符合正则表达式
                if (pwd.length() > 6) {//判断密码长度需大于6
                    if (TextUtils.equals(pwd, pwdCom)) { //两次输入密码一致
                        if (mDBOpenHelper.queryCountByAccount(uname) == 0) {    //输入的账号在数据库中不存在
                            //弹出验证码窗口
//                            CheckCode dialog = new CheckCode(this);
//                            dialog.show();
//                            dialog.setDialogSize();
                            //向数据库中插入数据
                            mDBOpenHelper.insert(uname, pwd, name);
                            //提示注册成功
                            Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
                            //跳转到登录后页面
                            Intent intent = this.getIntent();
                            Bundle bundle = intent.getExtras();
                            bundle.putString("aaa", uname); //添加要返回给登录的数据
                            intent.putExtras(bundle);
                            this.setResult(LoginActivity.RESULT_OK, intent);    //返回登录页面
                            this.finish();//销毁此Activity
                        } else {
                            Toast.makeText(this, "该账号已存在", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "密码位数不能小于6位", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "手机号格式错误，请重新输入", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "输入的账号密码不能为空", Toast.LENGTH_LONG).show();
        }
    }
    /*使用正则表达式验证手机号码*/
    public static boolean isMobile(String uname) {
        String REGEX_PHONE_NUMBER = "^(0(10|2\\d|[3-9]\\d\\d)[- ]{0,3}\\d{7,8}|0?1[3584]\\d{9})$";
        return Pattern.matches(REGEX_PHONE_NUMBER, uname);
    }

    /*读取照片操作*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitkat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://download/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }

            displayImage(imagePath);
        }
    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null
                , null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            signupImg.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied some permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}