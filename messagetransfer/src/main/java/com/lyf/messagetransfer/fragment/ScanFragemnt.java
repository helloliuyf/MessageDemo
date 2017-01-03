package com.lyf.messagetransfer.fragment;


import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lyf.messagetransfer.R;
import com.lyf.messagetransfer.bean.Barcode;
import com.lyf.messagetransfer.utils.ToastUtil;
import com.lyf.messagetransfer.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.pda.scan.ScanThread;

/**
 * Created by lyf on 2016/12/15 0015.
 * e_mail:helloliuyf@163.com
 */
public class ScanFragemnt extends BaseFragment {

    private EditText eidtBarCount;
    private Button buttonClear;
    private ListView listViewData;
    private CheckBox checkBoxPer;
    private Button buttonScan;
    private Button buttonExit;

    private ScanThread scanThread;
    private List<Barcode> listBarcode = new ArrayList<Barcode>();
    private List<Map<String, String>> listMap;
    private SimpleAdapter adapter = null;

    private Timer scanTimer = null;

    //handler接受数据,并在界面更新UI
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == ScanThread.SCAN) {
                // 获取的数据,
                String data = msg.getData().getString("data");

                // TODO:将数据发送给服务器
//				Toast.makeText(getApplicationContext(), data, 0).show();
                sortAndadd(listBarcode, data);
                addListView();
                eidtBarCount.setText(listBarcode.size() + "");
                Util.play(1, 0);
            }
        };
    };

    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_scan;
    }

    @Override
    public void initView() {
        eidtBarCount = (EditText) findView(R.id.editText_barcode_count);
        buttonClear = (Button) findView(R.id.button_clear);
        listViewData = (ListView) findView(R.id.listView_data_list);
        checkBoxPer = (CheckBox) findView(R.id.checkbox_per_100ms);
        buttonScan = (Button) findView(R.id.button_scan);
        buttonExit = (Button) findView(R.id.button_exit);
    }

    @Override
    public void initListener() {
        // 开始扫描
        buttonScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                scanThread.scan();

            }
        });
        // 自动扫描
        checkBoxPer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    buttonScan.setClickable(false);
                    buttonScan.setTextColor(Color.GRAY);
                    scanTimer = new Timer();
                    scanTimer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            if(scanThread != null){
                                scanThread.scan();
                            }

                        }
                    }, 100, 200);
                }else{
                    buttonScan.setClickable(true);
                    buttonScan.setTextColor(Color.BLACK);
                    if(scanTimer != null){
                        scanTimer.cancel();
                    }
                }

            }
        });

        // 清楚数据
        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                eidtBarCount.setText("");
                listBarcode.removeAll(listBarcode);
                listViewData.setAdapter(null);
            }
        });

        // 退出按钮
        buttonExit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getActivity().finish();
            }
        });
    }


    @Override
    public void initData() {
        try {
            scanThread = new ScanThread(mHandler);
        } catch (Exception e) {
            // 出现异常
            ToastUtil.showToast("serialport init fail");
            return;
            // e.printStackTrace();
        }
        scanThread.start();
        //init sound
        Util.initSoundPool(context);
    }



    private void addListView() {
        listMap = new ArrayList<Map<String, String>>();
        int id = 1;
        for (Barcode barcode : listBarcode) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", id + "");
            map.put("barcode", barcode.getBarcode());
            map.put("count", barcode.getCount() + "");
            listMap.add(map);
        }
        adapter = new SimpleAdapter(context, listMap, R.layout.listview_item,
                new String[] { "id", "barcode", "count", },
                new int[] { R.id.textView_list_item_id,
                R.id.textView_list_item_barcode,
                R.id.textView_list_item_count });

        listViewData.setAdapter(adapter);

    }

    // 扫描添加数据
    private List<Barcode> sortAndadd(List<Barcode> list, String barcode) {
        Barcode goods = new Barcode();
        goods.setBarcode(barcode);
        int temp = 1;
        if (list == null || list.size() == 0) {
            goods.setCount(temp);
            list.add(goods);
            return list;
        }

        for (int i = 0; i < list.size(); i++) {
            if (barcode.equals(list.get(i).getBarcode())) {
                temp = list.get(i).getCount() + temp;
                goods.setCount(temp);
                for (int j = i; j > 0; j--) {
                    list.set(j, list.get(j - 1));
                }
                list.set(0, goods);
                return list;
            }
        }
        //
        Barcode lastgoods = list.get(list.size() - 1);
        for (int j = list.size() - 1; j >= 0; j--) {
            if (j == 0) {
                goods.setCount(temp);
                list.set(j, goods);
            } else {
                list.set(j, list.get(j - 1));
            }

        }
        list.add(lastgoods);
        return list;
    }

    @Override
    public void onDestroy() {
        if(scanTimer!= null){
            scanTimer.cancel();
        }
        if (scanThread != null) {
            scanThread.interrupt();
            scanThread.close();
        }
        super.onDestroy();
    }
}
