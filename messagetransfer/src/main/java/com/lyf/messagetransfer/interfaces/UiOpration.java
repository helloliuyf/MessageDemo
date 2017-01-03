package com.lyf.messagetransfer.interfaces;

/**
 * Created by lyf on 2016/12/15 0015.
 * e_mail:helloliuyf@163.com
 */

public interface UiOpration {

    /** 返回一个布局id，用于设置正常界面 */
    int getContentViewLayoutId();

    /** 初始化View */
    void initView();

    /** 初始化监听器 */
    void initListener();

    /** 初始化数据 */
    void initData();
}
