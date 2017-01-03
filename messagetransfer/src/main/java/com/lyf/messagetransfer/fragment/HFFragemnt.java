package com.lyf.messagetransfer.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyf.messagetransfer.R;
import com.lyf.messagetransfer.utils.ToastUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.pda.serialport.SerialPort;

/**
 * Created by lyf on 2016/12/15 0015.
 * e_mail:helloliuyf@163.com
 */
public class HFFragemnt extends BaseFragment {

    private LinearLayout layoutSetting;
    private LinearLayout layout14443A;
    private LinearLayout layout15693;
    //	private TextView textViewSetting;
    private TextView textView14443A;
    private TextView textView15693;
    //	private View viewSettingBar;
    private View view14443ABar;
    private View view15693Bar;

    private FragmentManager fragManager;
    private FragmentTransaction fragTran;
    private Fragment iso14443AFragment;
    private Fragment iso15693Fragment;

    /** serialport **/
    private SerialPort mSerialport ;
    private int port ;
    private int buad ;
    private String powerStr ;
    private static InputStream is ;
    private static OutputStream os ;
    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_hf;
    }

    @Override
    public void initView() {
        layoutSetting = findView(R.id.linearLayoutSettingPara);
        layout14443A = findView(R.id.linearLayout14443A);
        layout15693 = findView(R.id.linearLayout15693);
//		textViewSetting = findViewById(R.id.textViewTabSetting);
        textView14443A = findView(R.id.textViewTab14443a);
        textView15693 = findView(R.id.textViewTa15693);
//		viewSettingBar = findView(R.id.viewSetting);
        view14443ABar = findView(R.id.view14443a);
        view15693Bar = findView(R.id.view15693);

        port = context.getSharedPreferences("MainSerialport", Context.MODE_PRIVATE).getInt("port", 13);
        buad = context.getSharedPreferences("MainSerialport", Context.MODE_PRIVATE).getInt("buad", 115200);
        powerStr = context.getSharedPreferences("MainSerialport", Context.MODE_PRIVATE).getString("power", "rfid power");
    }

    @Override
    public void initListener() {
        layout14443A.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("OnTouchListener", "layout14443A");
                textView14443A.setTextColor(getResources().getColor(R.color.tabSelect));
                view14443ABar.setBackgroundColor(getResources().getColor(R.color.tabSelect));
                textView15693.setTextColor(getResources().getColor(R.color.black));
                view15693Bar.setBackgroundColor(getResources().getColor(R.color.white));
                repleaceFragment(iso14443AFragment);
                return false;
            }
        });
        layout15693.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("OnTouchListener", "layout15693");
                textView14443A.setTextColor(getResources().getColor(R.color.black));
                view14443ABar.setBackgroundColor(getResources().getColor(R.color.white));
                textView15693.setTextColor(getResources().getColor(R.color.tabSelect));
                view15693Bar.setBackgroundColor(getResources().getColor(R.color.tabSelect));
                repleaceFragment(iso15693Fragment);
                return false;
            }
        });
    }

    @Override
    public void initData() {
        fragManager = getFragmentManager();
//		settingFragment = new SettingFragment();
        iso14443AFragment = new ISO14443AFragment();
        iso15693Fragment = new ISO15693Fragment();
        repleaceFragment(this.iso14443AFragment);

        try {
            mSerialport = new SerialPort(port, buad, 0);
        } catch (Exception e) {
            Log.e("", "Serialport init ERROR");
//			e.printStackTrace();
            ToastUtil.showToast("Serialport init ERROR");
            return;
        }
        if("3.3V".equals(powerStr)){
            mSerialport.power3v3on();
        }else if("5V".equals(powerStr)){
            mSerialport.power_5Von();
        }else if("scan power".equals(powerStr)){
            mSerialport.scaner_poweron();
        }else if("psam power".equals(powerStr)){
            mSerialport.psam_poweron();
        }else if("rfid power".equals(powerStr)){
            mSerialport.rfid_poweron();
        }
        is = mSerialport.getInputStream();
        os = mSerialport.getOutputStream();
        SharedPreferences.Editor editor = context.getSharedPreferences("MainSerialport", Context.MODE_PRIVATE).edit();
        editor.putInt("port", port);
        editor.putInt("buad", buad);
        editor.putString("power", powerStr);
        editor.commit();
    }

    //切换Fragment
    private void repleaceFragment(Fragment fragment){
        fragTran = fragManager.beginTransaction();
        fragTran.replace(R.id.layoutFragment, fragment);
        fragTran.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mSerialport != null){
            if("3.3V".equals(powerStr)){
                mSerialport.power3v3off();
            }else if("5V".equals(powerStr)){
                mSerialport.power_5Voff();
            }else if("scan power".equals(powerStr)){
                mSerialport.scaner_poweroff();
            }else if("psam power".equals(powerStr)){
                mSerialport.psam_poweroff();
            }else if("rfid power".equals(powerStr)){
                mSerialport.rfid_poweroff();
            }
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static final int REQUEST_SETTINT = 1101;



    public static InputStream getInputStream(){
        return is ;

    }

    public static OutputStream getOutputStream (){
        return os ;
    }
}
