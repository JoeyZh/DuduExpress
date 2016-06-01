package com.joey.expresscall.addfile;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.joey.expresscall.R;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.expresscall.file.bean.FileBean;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by Joey on 2016/5/23.
 */
public class ECSaveFileActivity extends BaseActivity {

	private final String keys[] = {"tag", "text", "access"};
	private final String tags[] = {"文件名：", "录制时长：","文件大小："};
	private final int ids[] = {R.id.item_text_tag, R.id.item_text,
			 R.id.item_access};
	private final int access[] = {R.drawable.ic_launcher,-1,-1};

	private ListView mListView;
	private ECSimpleAdapter1 mAdapter;
	private final int LIST_SIZE = 3;
	private ArrayList<HashMap<String,Object>> mData;
	private FileBean bean;
	private Button btnUpload;
	private EditText etExtraName;
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.save_btn_upload:
					if(checkUploadInfo()){
						upLoadFile();
					}
					break;
			}
		}
	};
	@Override
	public void initSettings() {
	}

	@Override
	public void initUi() {
		setContentView(R.layout.activity_save_file);
		bean  = (FileBean)statusHashMap.get("fileBean");
		mListView = (ListView) findViewById(R.id.save_file_list);
		mData = new ArrayList<HashMap<String, Object>>();
		String []contents = {"","",""};
		contents[0] = bean.getFileId();
		long minutes = TimeUnit.MILLISECONDS.toMinutes(bean.getDuration());
		long seconds = TimeUnit.MILLISECONDS.toSeconds(bean.getDuration())
				- TimeUnit.MINUTES.toSeconds(minutes);
		contents[1] = minutes + ":"+seconds;
		contents[2] = String.format("%.2f KB",bean.getFileLength()/1024.0f);
		for (int i = 0; i < LIST_SIZE; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(keys[0], tags[i]);
			map.put(keys[1], contents[i]);
			map.put(keys[2], -1);
			mData.add(map);
		}
		mAdapter = new ECSimpleAdapter1(this, mData,
				R.layout.simple_item_layout_1, keys, ids);
		mAdapter.setType(ECSimpleAdapter1.SIMPLE_ADAPTER_TYPE_TAG);
		mListView.setAdapter(mAdapter);

		btnUpload = (Button)findViewById(R.id.save_btn_upload);
		btnUpload.setOnClickListener(clickListener);
		etExtraName = (EditText)findViewById(R.id.save_edit_extra);

	}

	@Override
	public void saveSettings() {
		// TODO Auto-generated method stub

	}

	@Override
	public void freeMe() {
		statusHashMap.remove("fileBean");
	}

	private boolean checkUploadInfo(){
		String text = etExtraName.getText().toString();
		text = text.trim();
		if(text.isEmpty())
			return false;
		bean.setExtraName(text);
		return true;
	}
	private void upLoadFile(){
		MyLog.i("fileBean",bean.getMap().toString());
		ECCallManager.getInstance().upLoadCallFile(bean.getPath(),
				statusHashMap.get("username").toString(),
				"wav",
				bean.getExtraName(),
				bean.getDuration(),
				bean.getFileLength(),new ResponseListener<String>() {
			@Override
			public void onSuccess(String json) {
				ToastUtil.show(getApplicationContext(),R.string.upload_over);
			}

			@Override
			public void onError(RequestError error) {
				ToastUtil.show(getApplicationContext(),R.string.upload_error);
			}

			@Override
			public void onStart() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						createDialog(R.string.waiting,false);
					}
				});
			}

			@Override
			public void onFinish() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						dismissDialog();
					}
				});
			}
		});
	}
}
