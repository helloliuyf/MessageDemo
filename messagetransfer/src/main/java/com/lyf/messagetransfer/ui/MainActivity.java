package com.lyf.messagetransfer.ui;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.lyf.messagetransfer.R;
import com.lyf.messagetransfer.adapter.MainAdapter;
import com.lyf.messagetransfer.fragment.DeviceFragment;
import com.lyf.messagetransfer.fragment.HFFragemnt;
import com.lyf.messagetransfer.fragment.InspectionFragment;
import com.lyf.messagetransfer.fragment.MyFragment;
import com.lyf.messagetransfer.fragment.ScanFragemnt;
import com.lyf.messagetransfer.fragment.TaskFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ViewPager mVp;
    private RadioGroup mRg;


    @Override
    public int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mVp = findView(R.id.main_VP);
        mRg = (RadioGroup) findViewById(R.id.main_rg);
    }

    @Override
    public void initListener() {
        // RadioGroup点击的监听
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int position) {
                switch (position){
                    case R.id.main_rb_task:
                        mVp.setCurrentItem(0);

                        break;
                    case R.id.main_rb_inspection:
                        mVp.setCurrentItem(1);

                        break;
                    case R.id.main_rb_device:
                        mVp.setCurrentItem(2);

                        break;
                    case R.id.main_rb_my:
                        mVp.setCurrentItem(3);

                        break;
                }
            }
        });

        // 对ViewPager滑动的监听
        mVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mRg.check(R.id.main_rb_task);

                        break;
                    case 1:
                        mRg.check(R.id.main_rb_inspection);

                        break;
                    case 2:
                        mRg.check(R.id.main_rb_device);

                        break;
                    case 3:
                        mRg.check(R.id.main_rb_my);

                        break;

                }
            }
        });
        mVp.setCurrentItem(0);
    }

    @Override
    public void initData() {

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new TaskFragment());
        fragments.add(new InspectionFragment());
        fragments.add(new DeviceFragment());
        fragments.add(new MyFragment());

        MainAdapter mainAdapter = new MainAdapter(getSupportFragmentManager(), fragments);
        mVp.setAdapter(mainAdapter);

    }
}
