package com.lyf.messagetransfer.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;

/**
 * Created by lyf on 2016/12/19 0019.
 * e_mail:helloliuyf@163.com
 */

public class StreamUtil {

    // 将流转换成字符串
    public static String streamToString(InputStream inputStream) {

        try {

            //创建一个内存字节接受流
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            //将流转换成字符串
            String result = byteArrayOutputStream.toString();
            //关闭流
            inputStream.close();
            byteArrayOutputStream.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
