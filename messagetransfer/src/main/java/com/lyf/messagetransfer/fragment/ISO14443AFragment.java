package com.lyf.messagetransfer.fragment;

import java.io.InputStream;
import java.io.OutputStream;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lyf.messagetransfer.R;
import com.lyf.messagetransfer.utils.Util;

import cn.pda.rfid.hf.HfConmmand;
import cn.pda.rfid.hf.HfError;
import cn.pda.rfid.hf.HfReader;
import cn.pda.serialport.Tools;

public class ISO14443AFragment extends Fragment {
	private Context context ;
	private View rootView ;
	
	private HfConmmand hf ;
	private InputStream is ;
	private OutputStream os ;
	
	private TextView textTips ;
	private EditText editUID ;
	private EditText editSector ;
	private EditText editBlock ;
	private RadioButton radioA ;
	private RadioButton radioB ;
	private EditText editAccess ;
	private  EditText editReadData ;
	private EditText editWriteData ;
	private Button buttonFindCard ;
	private Button buttonAuth ;
	private Button buttonRead ;
	private Button buttonWrite ;
	
	
	private byte[] uid ;
	private int sector ;
	private int block ;
	private byte[] access ;
	private byte[] readData ;
	private byte[] writeData ;
	private int accessType ;
	
	private boolean authSuccess ;
	
	private boolean openSucceess = false ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
		Util.initSoundPool(context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_14443a, container, false);
		Log.e("ISO14443", "onCreateView");
		initView(rootView);
		return rootView;
	}
	
	private void initView(View rootView2) {
		textTips = (TextView ) rootView2.findViewById(R.id.textViewTips);
		editUID = (EditText ) rootView2.findViewById(R.id.editTextUid);
		editSector = (EditText ) rootView2.findViewById(R.id.editTextSector);
		editBlock = (EditText ) rootView2.findViewById(R.id.editTextBlock);
		radioA = (RadioButton ) rootView2.findViewById(R.id.radioButtonpassword_a);
		radioB = (RadioButton ) rootView2.findViewById(R.id.radioButtonpassword_b);
		editAccess = (EditText ) rootView2.findViewById(R.id.editTextPassword);
		editReadData = (EditText ) rootView2.findViewById(R.id.editTextReadData);
		editWriteData = (EditText ) rootView2.findViewById(R.id.editTextWriteData);
		buttonFindCard = (Button ) rootView2.findViewById(R.id.buttonFind14443A);
		buttonAuth = (Button ) rootView2.findViewById(R.id.buttonAuth);
		buttonRead = (Button ) rootView2.findViewById(R.id.buttonRead14443);
		buttonWrite = (Button ) rootView2.findViewById(R.id.buttonWrite14443);
		
		listener();
	}

	private void listener() {
		buttonFindCard.setOnClickListener(new MyOnclickListener());
		buttonAuth.setOnClickListener(new MyOnclickListener());
		buttonRead.setOnClickListener(new MyOnclickListener());
		buttonWrite.setOnClickListener(new MyOnclickListener());
		
		radioA.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				accessType = HfReader.AUTH_A;
				
			}
		});
		radioB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				accessType = HfReader.AUTH_B;
				
			}
		});
	}

	@Override
	public void onResume() {
		is = HFFragemnt.getInputStream();
		os = HFFragemnt.getOutputStream();
		if(is == null || os == null){
			textTips.setText(R.string.tips_init_serialport_fail);
			openSucceess = false ;
		}else{
			textTips.setText("");
			openSucceess = true ;
			hf = new HfReader(is, os); 
		}
		Log.e("ISO14443", "onResume");
		super.onResume();
	}
	
	private class MyOnclickListener implements OnClickListener{
		HfError error = new HfError();
		@Override
		public void onClick(View v) {
			if(openSucceess){
				String sectorStr = editSector.getText().toString();
				String blockStr = editBlock.getText().toString();
				String accessStr = editAccess.getText().toString();
				String writeDataStr = editWriteData.getText().toString();
				sector = Integer.valueOf(sectorStr);
				block = Integer.valueOf(blockStr);
				access = Tools.HexString2Bytes(accessStr);
				writeData = Tools.HexString2Bytes(writeDataStr);
				switch (v.getId()) {
				case R.id.buttonFind14443A:  // Ѱ��
					uid = hf.findCard14443A(error);
					if(uid != null){
						editUID.setText(Tools.Bytes2HexString(uid, uid.length));
						Util.play(1, 0);
					}else{
						Toast.makeText(context, "find card fail ,Error code " + error.getErrorCode(), 0).show();
					}
					break;
				case R.id.buttonAuth:
					if(uid != null){

						if(hf.auth14443A(accessType, access, uid, sector*4, error) == 0){
							authSuccess = true ;
							Toast.makeText(context, "Auth card success", 0).show();
							Util.play(1, 0);
						}else{
							Toast.makeText(context, "Auth card fail ,Error code " + error.getErrorCode(), 0).show();
							authSuccess = false ;
						}
					}
					break;
				case R.id.buttonRead14443:
					if(authSuccess){

						readData = hf.read14443A(sector*4 + block, error);
						if(readData != null){
							editReadData.setText(Tools.Bytes2HexString(readData, readData.length));
							Util.play(1, 0);
						}else{
							Toast.makeText(context, "Read card data fail ,Error code " + error.getErrorCode(), 0).show();
						}
					}else{
						Toast.makeText(context, "Please auth card", 0).show();
						authSuccess = false ;
					}
					break;
				case R.id.buttonWrite14443:
					if(authSuccess){
						/*
						 * 
						 */
						if(hf.write14443A(writeData, sector*4 + block, error) == 0){
							Toast.makeText(context, "Write data success", 0).show();
							Util.play(1, 0);
						}else{
							Toast.makeText(context, "Write data fail,Error code " + error.getErrorCode(), 0).show();
						}
					}else{
						Toast.makeText(context, "Please auth card,Error code " + error.getErrorCode(), 0).show();
						authSuccess = false ;
					}
					break;

				default:
					break;
				}
			}else{
				Toast.makeText(context, R.string.tips_init_serialport_fail, 0).show();
			}
			
		}
		
	}
	

}
