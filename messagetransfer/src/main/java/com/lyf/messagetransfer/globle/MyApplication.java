package com.lyf.messagetransfer.globle;

import android.app.Application;
import android.content.Context;

/**
 * Created by lyf on 2016/12/16 0016.
 * e_mail:helloliuyf@163.com
 */

public class MyApplication extends Application {

    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
