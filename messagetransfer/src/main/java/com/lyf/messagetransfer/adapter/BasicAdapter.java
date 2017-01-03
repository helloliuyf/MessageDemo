package com.lyf.messagetransfer.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lyf.messagetransfer.holder.BaseHolder;

import java.util.ArrayList;

/**
 * Created by lyf on 2016/12/23 0023.
 * e_mail:helloliuyf@163.com
 */

public abstract class BasicAdapter<T> extends BaseAdapter {

    private ArrayList<T> list;
    public BasicAdapter (ArrayList<T> list){
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // 初始化BaseHolder
        BaseHolder<T> holder = null;
        if (convertView == null) {
            holder = getHolder(position);
        }else {
            holder = (BaseHolder<T>) convertView.getTag();
        }
        // 绑定数据
        T t = list.get(position);
        holder.bindData(t);
        return holder.holderView;
    }

    // 具体的holder由子类实现
    protected abstract BaseHolder<T> getHolder(int position);





}
