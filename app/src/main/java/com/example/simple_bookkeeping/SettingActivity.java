package com.example.simple_bookkeeping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.simple_bookkeeping.db.DBManager;
import com.example.simple_bookkeeping.utils.CheckCode;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_iv_back:
                finish();
                break;
            case R.id.setting_tv_clear:
//                CheckCode dialog = new CheckCode(this);
//                dialog.show();
//                dialog.setDialogSize();
                showDeleteDialog();
                break;
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除提示")
                .setMessage("您确定要删除所有记录吗?\n注意删除后无法恢复，请慎重选择")
                .setPositiveButton("取消", null)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBManager.deleteAllAccount();
                        Toast.makeText(SettingActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }
}