package com.example.simple_bookkeeping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

/*启动页*/
public class SplashActivity extends AppCompatActivity {
    private Button skipBtn;
    private Handler skipHandler = new Handler();
    private Runnable skipRunnable = new Runnable() {
        @Override
        public void run() {
            toLoginActivity();
            skipHandler.removeCallbacks(skipRunnable);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initEvent();
        skipHandler.postDelayed(skipRunnable, 2000);
    }

    private void initEvent() {
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLoginActivity();
            }
        });
    }

    private void initView() {
        skipBtn = findViewById(R.id.splash_btn_skip);
    }

    public void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        skipHandler.removeCallbacks(skipRunnable);
//    }
}