package com.example.simple_bookkeeping.toexcel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simple_bookkeeping.R;
import com.example.simple_bookkeeping.db.AccountBean;
import com.example.simple_bookkeeping.db.DBManager;
import com.example.simple_bookkeeping.db.TypeBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ToExcelActivity extends Activity implements View.OnClickListener {
    private Button exportButton;
    private Button openButton;
    private TextView textView;
    private AlertDialog alertDialog;

    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private int REQUEST_PERMISSION_CODE = 1000;

    private String filePath = "/sdcard/Download";

    private void requestPermission() {
        if (Build.VERSION.SDK_INT > 23) {
            if (ContextCompat.checkSelfPermission(ToExcelActivity.this,
                    permissions[0])
                    == PackageManager.PERMISSION_GRANTED) {
                //授予权限
                Log.i("requestPermission:", "用户之前已经授予了权限！");
            } else {
                //未获得权限
                Log.i("requestPermission:", "未获得权限，现在申请！");
                requestPermissions(permissions
                        , REQUEST_PERMISSION_CODE);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_excel);
        requestPermission();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        exportButton = findViewById(R.id.export_button);
        exportButton.setOnClickListener(this);

        openButton = findViewById(R.id.open_button);
        openButton.setOnClickListener(this);
        textView = findViewById(R.id.textView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("onPermissionsResult:", "权限" + permissions[0] + "申请成功");
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {
                Log.i("onPermissionsResult:", "用户拒绝了权限申请");
                AlertDialog.Builder builder = new AlertDialog.Builder(ToExcelActivity.this);
                builder.setTitle("permission")
                        .setMessage("点击允许才可以使用我们的app哦")
                        .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (alertDialog != null && alertDialog.isShowing()) {
                                    alertDialog.dismiss();
                                }
                                ActivityCompat.requestPermissions(ToExcelActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                        });
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }

    private void showDialogTipUserRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.export_button:
                exportExcel(this);
                break;
            case R.id.open_button:
                openDir();
            default:
                break;
        }
    }

    private void openDir() {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setDataAndType(Uri.fromFile(file), "file/*");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "没有正确打开文件管理器", Toast.LENGTH_SHORT).show();
        }
    }


    private void exportExcel(Context context) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        String excelFileName = "/demo.xls";
        String[] title = {"ID", "类型", "图片", "备注", "金额", "时间", "年", "月", "日", "收支"};
        String sheetName = "demoSheetName";

        //获取数据库当中的数据源
        List<DemoBean> demoBeanList = new ArrayList<>();
        List<DemoBean> inList = DBManager.getAllToExcel();
        demoBeanList.addAll(inList);

        filePath = filePath + excelFileName;

        ExcelUtil.initExcel(filePath, sheetName, title);

        ExcelUtil.writeObjListToExcel(demoBeanList, filePath, context);

        textView.setText("excel已导出至：" + filePath);

    }
}