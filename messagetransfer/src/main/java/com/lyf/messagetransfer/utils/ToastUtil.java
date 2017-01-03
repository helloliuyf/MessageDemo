package com.lyf.messagetransfer.utils;

import android.widget.Toast;

import com.lyf.messagetransfer.globle.MyApplication;

/**
 * Created by lyf on 2016/12/16 0016.
 * e_mail:helloliuyf@163.com
 */

public class ToastUtil {

    private static Toast toast;
    /**
     * @param text
     */
    public static void showToast(String text){
        if(toast==null){
            //创建吐司对象
            toast = Toast.makeText(MyApplication.context, text, Toast.LENGTH_SHORT);
        }else {
            //说明吐司已经存在了，那么则只需要更改当前吐司的文字内容
            toast.setText(text);
        }
        //最后你再show
        toast.show();
    }
}
