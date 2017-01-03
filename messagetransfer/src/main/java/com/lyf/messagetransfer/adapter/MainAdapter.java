package com.lyf.messagetransfer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by lyf on 2016/12/15 0015.
 * e_mail:helloliuyf@163.com
 */
public class MainAdapter extends FragmentPagerAdapter{

    private ArrayList<Fragment> fragments;
    public MainAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
