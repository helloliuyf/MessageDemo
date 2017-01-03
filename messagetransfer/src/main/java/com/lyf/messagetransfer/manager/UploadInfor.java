package com.lyf.messagetransfer.manager;

import java.io.File;

/**
 * Created by lyf on 2016/12/23 0023.
 * e_mail:helloliuyf@163.com
 */

public class UploadInfor {

    public String name;//上传任务的标识，存取的时候用到，我们会使用文件的名称来做识别依据
    public long uploadLength;//已经上传的大小
    public long size;//总大小
    public String filePath;
    public int state;


    // 快速初始化UploadInfor的方法
    public static UploadInfor create(File file){

        if (file.isFile() && file.exists()) {
            UploadInfor uploadInfor = new UploadInfor();
            uploadInfor.name = file.getName().toString();
            uploadInfor.size = file.length();
            uploadInfor.filePath = file.getAbsolutePath(); //绝对路径
            uploadInfor.uploadLength = 0;
            uploadInfor.state = UploadManager.STATE_NONE;//一开始是未下载的状态
            return uploadInfor;
        }
        return null;
    }
}
