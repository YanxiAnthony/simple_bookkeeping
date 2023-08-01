package com.example.simple_bookkeeping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simple_bookkeeping.adapter.AccountAdapter;
import com.example.simple_bookkeeping.db.AccountBean;
import com.example.simple_bookkeeping.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ListView searchLv;
    EditText searchEt;
    TextView emptyTv;
    List<AccountBean> mDatas;   //数据源
    AccountAdapter adapter;    //适配器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        mDatas=new ArrayList<>();
        adapter=new AccountAdapter(this,mDatas);
        searchLv.setAdapter(adapter);
        searchLv.setEmptyView(emptyTv);     //设置无数据时显示的布局控件
    }

    private void initView() {
        searchLv = findViewById(R.id.search_lv);
        searchEt = findViewById(R.id.search_et);
        emptyTv = findViewById(R.id.search_tv_empty);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_sh:     //执行搜索操作
                String msg = searchEt.getText().toString().trim();  //去除回车空格等非数据
                //判断输入内容是否为空，如果为空则不能搜索
                if (TextUtils.isEmpty(msg)) {
                    Toast.makeText(this,"输入内容不能为空",Toast.LENGTH_SHORT);
                    return;
                }
                //输入内容不为空，开始搜索
                List<AccountBean> list = DBManager.getAccountListByRemarkFromAccounttb(msg);
                mDatas.clear();
                mDatas.addAll(list);
                adapter.notifyDataSetChanged();
                break;
        }
    }
}