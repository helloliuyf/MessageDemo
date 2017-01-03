package com.lyf.messagetransfer.manager;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.lyf.messagetransfer.globle.MyApplication;
import com.lyf.messagetransfer.utils.HttpUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lyf on 2016/12/23 0023.
 * e_mail:helloliuyf@163.com
 */

public class UploadManager {

    // 上传的地址
    public static String path;


    //记录所有的UploadInfor，存储的是每个文件的文件名
    private HashMap<String, UploadInfor> uploadInfoMap = new HashMap<String, UploadInfor>();

    // 上传的文件的基本地址
    public static String FILE_BASEPATH = Environment.getExternalStorageDirectory() + File.separator +
            MyApplication.context.getPackageName() + File.separator + "upload/";

    //1.定义状态常亮
    public static final int STATE_NONE = 0;//未上传
    public static final int STATE_UPLOADING = 1;//上传中
    public static final int STATE_FINISH = 3;//上传完成
    public static final int STATE_ERROR = 4;//上传出错
    public static final int STATE_WAITING = 5;//等待中，任务创建并添加到线程池，但是run方法没有执行

    private Handler handler = new Handler(Looper.getMainLooper());

    //用来存放所有的DownloadObserver对象
    private ArrayList<UploadObserver> observerList = new ArrayList<UploadObserver>();

    private URL url = null;
    /**
     * 单例模式
     */
    private static UploadManager mInstance = new UploadManager();

    public static UploadManager getInstance() {
        return mInstance;
    }

    private UploadManager() {
        try {
            // 封装url上传地址,如果上传地址是固定的,则不用从外界传入,如果不是固定的,则需要从外界传入
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // 上传的方法,我们是上传的一个数据库文件或是json文件
    public void upload(String fileName) {
        // 我们根据文件存储的位置和文件的名字,来确定文件
        String filePath = FILE_BASEPATH + fileName;

        //1.根据一个任务的state来进行判断操作(有可能没有下载过，也有可能下载过一半，也有可能下载完)
        File file = new File(filePath);

        // 如果文件存在或是文件的size大于0,表示有合适的数据,才进行上传
        if (file.isFile() && file.length() > 0) {

            // 通过文件名来获取该文件的上传信息
            UploadInfor uploadInfor = uploadInfoMap.get(fileName);

            // 如果uploadInfor不存在,说明该文件没有被上传,则需要创建上传信息,并且存起来
            if (uploadInfor == null) {
                uploadInfor = UploadInfor.create(file);

                uploadInfoMap.put(fileName, uploadInfor);
            }
            // 如果文件没有上传或是文件上传失败,则重新创建上传任务
            if (uploadInfor.state == STATE_NONE || uploadInfor.state == STATE_ERROR) {

                //将uploadInfor的state设置等待中
                uploadInfor.state = STATE_WAITING;

                //可以进行上传,创建上传任务，添加到线程池中
                UploadTask uploadTask = new UploadTask(uploadInfor);

                //将状态变化通知给外界的监听器
                notifyUploadStateChange(uploadInfor);

                //交给线程池管理
                ThreadPoolManager.getInstance().execute(uploadTask);
            }

        }
    }

    class UploadTask implements Runnable {

        private UploadInfor uploadInfor;

        public UploadTask(UploadInfor uploadInfor) {
            this.uploadInfor = uploadInfor;
        }



        @Override
        public void run() {
            // 只要run方法一执行,就将上传状态改为正在上传
            uploadInfor.state = STATE_UPLOADING;
            //将状态变化通知给外界的监听器
            notifyUploadStateChange(uploadInfor);
            //获取文件的路径
            String filePaths = uploadInfor.filePath;
            File file = new File(filePaths);
            // 如果上传的文件存在,则上传
            if (file.exists()) {
                try {
                    int[] statusCodes = HttpUtil.getInstance().postFileToNet(path, filePaths, uploadInfor.name);
                    // 如果返回码是200,则表示上传成功
                    if (statusCodes[0] == 200) {
                        uploadInfor.state = STATE_FINISH;
                        notifyUploadStateChange(uploadInfor);
                        // 只要上传成功,就把文件删除
                        file.delete();
                    } else {
                        // 只要不是200,就表示上传没有成功
                        uploadInfor.state = STATE_ERROR;
                        notifyUploadStateChange(uploadInfor);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 通知所有的监听器状态更改了
     * @param uploadInfor
     */
    public void notifyUploadStateChange(final UploadInfor uploadInfor) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (UploadObserver observer : observerList) {
                    observer.onUploadStateChange(uploadInfor);
                }
            }
        });
    }

    /**
     * 添加一个下载监听器对象
     * @param observer
     */
    public void registerUploadObserver(UploadObserver observer){
        if(!observerList.contains(observer)){
            observerList.add(observer);
        }
    }

    /**
     * 移除一个下载监听器对象
     * @param observer
     */
    public void unregisterUploadObserver(UploadObserver observer){
        if(observerList.contains(observer)){
            observerList.remove(observer);
        }
    }

    /**
     * 定义下载监听器，目的是暴露自己下载的状态和进度
     */
    public interface UploadObserver{
        /**
         * 当下载状态改变，包括进度改变
         */
        void onUploadStateChange(UploadInfor uploadInfor);
    }
}
