package com.lyf.messagetransfer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyf.messagetransfer.interfaces.UiOpration;

/**
 * Created by lyf on 2016/12/15 0015.
 * e_mail:helloliuyf@163.com
 */

public abstract class BaseFragment extends Fragment implements UiOpration{
    protected Context context;
    protected ViewGroup rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        rootView = (ViewGroup) inflater.inflate(getContentViewLayoutId(), null);
        initView();
        initListener();
        initData();
        return rootView;
    }

    public <T> T findView(int id) {
        T view = (T) rootView.findViewById(id);
        return view;
    }
}
