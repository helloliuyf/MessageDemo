package com.lyf.messagetransfer.fragment;

import android.widget.TextView;

import com.lyf.messagetransfer.R;

/**
 * Created by lyf on 2017/1/3 0003.
 * e_mail:helloliuyf@163.com
 */

public class DeviceFragment extends BaseFragment {
    private TextView tv;
    @Override
    public int getContentViewLayoutId() {
        return R.layout.layout_fragment;
    }

    @Override
    public void initView() {
        tv = findView(R.id.tv);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        tv.setText(getClass().getSimpleName());
    }
}
