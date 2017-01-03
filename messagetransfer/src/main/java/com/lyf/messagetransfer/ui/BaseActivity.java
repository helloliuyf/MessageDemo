package com.lyf.messagetransfer.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lyf.messagetransfer.interfaces.UiOpration;

/**
 * Created by lyf on 2016/12/15 0015.
 * e_mail:helloliuyf@163.com
 */

public abstract class BaseActivity extends AppCompatActivity implements UiOpration{

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;
        setContentView(getContentViewLayoutId());
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        initView();
        initListener();
        initData();
    }

    public <T> T findView(int id) {
        T view = (T) findViewById(id);
        return view;
    }

    // 返回键退出程序
    long exitSytemTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitSytemTime > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitSytemTime = System.currentTimeMillis();
                return true;
            } else {
                finish();
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}
