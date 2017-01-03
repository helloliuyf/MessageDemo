package com.lyf.messagetransfer.adapter;

import com.lyf.messagetransfer.bean.Barcode;
import com.lyf.messagetransfer.holder.BaseHolder;
import com.lyf.messagetransfer.holder.MyHolder;

import java.util.ArrayList;

/**
 * Created by lyf on 2016/12/23 0023.
 * e_mail:helloliuyf@163.com
 */

public class MyAdapter extends BasicAdapter<Barcode> {

    public MyAdapter(ArrayList<Barcode> list) {
        super(list);
    }

    @Override
    protected BaseHolder<Barcode> getHolder(int position) {
        return new MyHolder();
    }
}
