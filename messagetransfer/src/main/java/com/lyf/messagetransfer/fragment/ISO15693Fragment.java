package com.lyf.messagetransfer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lyf.messagetransfer.R;
import com.lyf.messagetransfer.utils.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.pda.rfid.hf.Error;
import cn.pda.rfid.hf.HfConmmand;
import cn.pda.rfid.hf.HfError;
import cn.pda.rfid.hf.HfReader;
import cn.pda.rfid.hf.Iso15693CardInformation;
import cn.pda.rfid.hf.Iso15693InventoryInfo;
import cn.pda.serialport.Tools;

public class ISO15693Fragment extends Fragment {
	private Context context;
	private View rootView;

	/***
	 * UI
	 **/
	private TextView textTips;
	private EditText editInfo;
	private EditText editCardCout;
	private Spinner spinnerUID;
	private TextView textBlock;
	private TextView textBlockLen;
	private Spinner spinnerSelectBlock;
	private EditText editWriteData;
	private Button buttonWriteData;
	private Button buttonFindCard;
	private Button buttonGetCardInfo;
	private Button buttonReadData;
	private Button buttonClear;

	private InputStream is;
	private OutputStream os;
	private HfConmmand hf;
	private boolean openSuccess = false;

	private List<String> listUid;
	private ArrayAdapter<String> adapterUid;
	private List<Iso15693InventoryInfo> listInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		context = getActivity();
		Util.initSoundPool(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_15693, container, false);
		initView(rootView);
		return rootView;
	}

	private void initView(View rootView2) {
		textTips = (TextView) rootView2.findViewById(R.id.textViewTips15693);
		editInfo = (EditText) rootView2.findViewById(R.id.editTextInfo);
		textBlock = (TextView) rootView2.findViewById(R.id.editTextBlocks);
		textBlockLen = (TextView) rootView2.findViewById(R.id.editTextSingleBlockLen);
		editCardCout = (EditText) rootView2.findViewById(R.id.editTextFindCardCount);
		editWriteData = (EditText) rootView2.findViewById(R.id.editTextWriteData15693);
		spinnerSelectBlock = (Spinner) rootView2.findViewById(R.id.spinnerBlock);
		spinnerUID = (Spinner) rootView2.findViewById(R.id.spinnerSelectCard);
		buttonClear = (Button) rootView2.findViewById(R.id.buttonClear15693);
		buttonFindCard = (Button) rootView2.findViewById(R.id.buttonFind15693);
		buttonGetCardInfo = (Button) rootView2.findViewById(R.id.buttonGetCardInfo);
		buttonReadData = (Button) rootView2.findViewById(R.id.buttonRead15693);
		buttonWriteData = (Button) rootView2.findViewById(R.id.buttonWriteData15693);

		listener();
	}

	@Override
	public void onResume() {
		is = HFFragemnt.getInputStream();
		os = HFFragemnt.getOutputStream();
		if (is == null || os == null) {
			textTips.setText(R.string.tips_init_serialport_fail);
			openSuccess = false;
		} else {
			textTips.setText("");
			openSuccess = true;
			hf = new HfReader(is, os);
		}
		super.onResume();
	}

	private void listener() {
		buttonClear.setOnClickListener(new MyClick());
		buttonFindCard.setOnClickListener(new MyClick());
		buttonGetCardInfo.setOnClickListener(new MyClick());
		buttonReadData.setOnClickListener(new MyClick());
		buttonWriteData.setOnClickListener(new MyClick());

		spinnerUID.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				uid = listInfo.get(position).getUid();
				flags = listInfo.get(position).getFlag() & 0xff;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinnerSelectBlock.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
									   int position, long id) {
				block = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}


	private void updateInventoryInfo() {
		if (listInfo != null && !listInfo.isEmpty()) {
			editCardCout.setText("" + listInfo.size());
			editInfo.append("\nFind ISO15693 card :" + "\n");
			listUid = new ArrayList<String>();
			Util.play(1, 0);
			for (Iso15693InventoryInfo info : listInfo) {
				listUid.add(Tools.Bytes2HexString(info.getUid(), info.getUid().length));
				editInfo.append("UID:" + Tools.Bytes2HexString(info.getUid(), info.getUid().length) + "\n");
				editInfo.append("DSFID: 0x" + Tools.Bytes2HexString(new byte[]{info.getDsfid()}, 1) + "\n");
				editInfo.append("FLAG: 0x" + Tools.Bytes2HexString(new byte[]{info.getFlag()}, 1) + "\n");
			}
			adapterUid = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_dropdown_item,
					listUid);
			spinnerUID.setAdapter(adapterUid);
		} else {
			editCardCout.setText("");
			editInfo.append("Find card fail , Error code :" + error.getErrorCode() + "\n");
		}
	}

	private void updateGetCardInfo() {
		if (cardInfo != null) {
			Util.play(1, 0);
			textBlock.setText(cardInfo.getBlocksCount() + "");
			textBlockLen.setText((cardInfo.getBlockLen() + 1) + "");
			int allBlock = cardInfo.getBlocksCount() & 0xff;
			List<String> listBlock = new ArrayList<String>();
			for (int i = 0; i <= allBlock; i++) {
				listBlock.add(i + "");
			}
			spinnerSelectBlock.setAdapter(new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_dropdown_item,
					listBlock));
			editInfo.append("\n Get card PICC:\n");
			editInfo.append("UID:" + Tools.Bytes2HexString(cardInfo.getUid(), cardInfo.getUid().length) + "\n");
			editInfo.append("AFI:0x" + Tools.Bytes2HexString(new byte[]{cardInfo.getAfi()}, 1) + "\n");
			editInfo.append("DSFID:0x" + Tools.Bytes2HexString(new byte[]{cardInfo.getDsfid()}, 1) + "\n");
			editInfo.append("FLAG:0x" + Tools.Bytes2HexString(new byte[]{cardInfo.getFlag()}, 1) + "\n");

		} else {
			textBlock.setText("");
			textBlockLen.setText("");
			spinnerSelectBlock.setAdapter(null);
			editInfo.append("Get card info fail , Error code :" + error.getErrorCode() + "\n");
		}
	}

	private void updateReadData() {
		if (readData != null) {
			Util.play(1, 0);
			editInfo.append("\n Read card single blcok data:\n");
			editInfo.append("DATA:0x" + Tools.Bytes2HexString(readData, readData.length) + "\n");
		} else {
			editInfo.append("Read data fail , Error code :" + error.getErrorCode() + "\n");
		}
	}

	private void updateWriteData() {
		if (writeFlag == 0) {
			Util.play(1, 0);
			editInfo.append("\n Read card single blcok data:\n");
			editInfo.append("Write data success\n");
		} else {
			editInfo.append("Write data fail , Error code :" + error.getErrorCode() + "\n");
		}
	}

	Error error = new HfError();
	private byte[] uid = null;
	private int flags = 0;
	private Iso15693CardInformation cardInfo;
	private int block = 0;
	private byte[] readData;
	private byte[] writeData;
	private int writeFlag;

	private class MyClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (!openSuccess) {
				textTips.setText(R.string.tips_init_serialport_fail);
				Toast.makeText(context, R.string.tips_init_serialport_fail, Toast.LENGTH_SHORT).show();
				return;
			}


			switch (v.getId()) {
				case R.id.buttonFind15693:
					listInfo = hf.findCard15693(error);
					updateInventoryInfo();
					break;
				case R.id.buttonGetCardInfo:
					if (uid != null) {
						cardInfo = hf.getInformation15693(uid, flags, error);
						updateGetCardInfo();
					} else {
						Toast.makeText(context, R.string.tips_find_card, Toast.LENGTH_SHORT).show();
					}

					break;
				case R.id.buttonRead15693:
					if (uid != null) {
						readData = hf.readSingleBlock15693(uid, flags, block, error);
						updateReadData();
					} else {
						Toast.makeText(context, R.string.tips_find_card, Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.buttonWriteData15693:
					if (uid != null) {
						if (checkWriteData()) {
							writeFlag = hf.writeSingleBlock(uid, flags, block, writeData, error);
							updateWriteData();
						} else {
							Toast.makeText(context, R.string.tips_input_right_data, Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(context, R.string.tips_find_card, Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.buttonClear15693:
					editInfo.setText("");
					break;

				default:
					break;
			}

		}

		private boolean checkWriteData() {
			String writeDataStr = editWriteData.getText().toString();
			boolean flag = false;
			String regString = "[a-f0-9A-F]{8}";
			flag = Pattern.matches(regString, writeDataStr);
			if (flag) {
				writeData = Tools.HexString2Bytes(writeDataStr);
			}
			return flag;
		}

	}
}
