package com.lyf.messagetransfer.holder;

import android.view.View;

/**
 * Created by lyf on 2016/12/23 0023.
 * e_mail:helloliuyf@163.com
 */

public abstract class BaseHolder<T> {

    public View holderView;
    public BaseHolder (){
        holderView = getHolderView();
        holderView.setTag(this);
    }
    // 具体的布局交由子类实现
    protected abstract View getHolderView();
    // 绑定具体的数据交由子类实现
    public abstract void bindData(T t);

}
