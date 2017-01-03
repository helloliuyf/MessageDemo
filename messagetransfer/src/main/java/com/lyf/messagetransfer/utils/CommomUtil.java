package com.lyf.messagetransfer.utils;

import com.lyf.messagetransfer.globle.MyApplication;

/**
 * Created by lyf on 2016/12/20 0020.
 * e_mail:helloliuyf@163.com
 */

public class CommomUtil {
    /**
     * 获取xml中的数组
     * @param resId
     * @return
     */
    public static String[] getStringArray(int resId){
        return MyApplication.context.getResources().getStringArray(resId);
    }

    /**
     * 获取xml中的String
     * @param resId
     * @return
     */
    public static String getString(int resId){
        return MyApplication.context.getResources().getString(resId);
    }
}
