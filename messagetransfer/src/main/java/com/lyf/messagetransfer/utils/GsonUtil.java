package com.lyf.messagetransfer.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by lyf on 2016/12/19 0019.
 * e_mail:helloliuyf@163.com
 */

public class GsonUtil {
    /**
     * 把一个json字符串变成对象
     * @param json
     * @param cls
     * @return
     */
    /*public static <T> T parseJsonToBean(String json, Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }*/
    // 这种写法是模仿Gson中对json字符串转化为一个类的方法
    public static <T> T parseJsonToBean(String json,Class<T> cls) {
        Gson gson = new Gson();
        T t = null;
        t = gson.fromJson(json,cls);

        return t;
    }

    public static List<?> parseJsonToList(String json, Type type){
        Gson gson = new Gson();
        List<?> list = gson.fromJson(json,type);
        return list;
    }
    // 将一个对象转换成一个json字符串
    public static String toJson(Object obj){
        if(obj==null)return "";
        return new Gson().toJson(obj);
    }
}
