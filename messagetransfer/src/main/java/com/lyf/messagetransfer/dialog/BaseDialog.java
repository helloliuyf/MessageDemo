package com.lyf.messagetransfer.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.lyf.messagetransfer.R;

/**
 * Created by lyf on 2016/12/21 0021.
 * e_mail:helloliuyf@163.com
 */

public abstract class BaseDialog extends Dialog {

    public BaseDialog(Context context) {
        // 参一是上下文; 参二是dialog的自定义样式
        this(context, R.style.BottomDialog);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    // 重写onCreat方法
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置dialog位于整个屏幕的底部
        getWindow().setGravity(Gravity.BOTTOM);
        // 去掉dialog的标题栏
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // 设置dialog的具体内容
        setContentView(getLayoutId());

        /**
         * 设置整个dialog的宽
         */
        // 获取窗口管理者
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        // 获取屏幕的宽
        int width = windowManager.getDefaultDisplay().getWidth();
        // 获取窗口管理者的属性
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        // 将屏幕的宽赋值给窗口的宽属性
        attributes.width = width;
        getWindow().setAttributes(attributes);
    }
    // 具体的布局由子类实现
    public abstract int getLayoutId();
}
