package com.lyf.messagetransfer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.lyf.messagetransfer.R;
import com.lyf.messagetransfer.utils.HttpUtil;

import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 执行了一个延时任务,跳转主页面
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        },2000);
        String url = "";
        HttpUtil.getInstance().getJsonFromNet(this, url, new HttpUtil.CallBackDepdependJson() {
            @Override
            public void onSuccess(JSONObject successJson) {

            }

            @Override
            public void onFail(JSONObject failJson) {

            }
        });
    }
}
