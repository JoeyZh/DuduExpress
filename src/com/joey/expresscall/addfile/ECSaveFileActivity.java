package com.joey.expresscall.addfile;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.expresscall.file.bean.FileBean;
import com.joey.expresscall.player.PlaybackFragment;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseActivity;
import com.joey.general.utils.FileUtil;
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
	private boolean loadSuccess;

	private ListView mListView;
	private ECSimpleAdapter1 mAdapter;
	private final int LIST_SIZE = 3;
	private ArrayList<HashMap<String,Object>> mData;
	private FileBean bean;
	private Button btnUpload;
	private EditText etExtraName;
	private AlertDialog quitDialog;
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.save_btn_upload:
					if(checkUploadInfo()){
						upLoadFile();
					}
					break;
				case R.id.right_btn:
					play(bean);
					break;
				case R.id.left_btn:
					onBackPressed();
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
		getTopBarView().setTopBar(R.drawable.icon_back,-1,R.string.upload,clickListener);
		getTopBarView().setRightTextRes(R.string.listening);

		bean  = (FileBean)statusHashMap.get("fileBean");
		mListView = (ListView) findViewById(R.id.save_file_list);
		mData = new ArrayList<HashMap<String, Object>>();
		String []contents = {"","",""};
		contents[0] = bean.getFileName();
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
		if(loadSuccess)
			statusHashMap.put("fileBean",bean);
		else
			statusHashMap.remove("fileBean");
	}

	@Override
	public void freeMe() {

	}

	private AlertDialog createQuitDialog(){
		if(quitDialog != null){
			return quitDialog;
		}
		quitDialog = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setTitle(R.string.warn_title)
				.setMessage(R.string.saving_notice)
				.setNegativeButton(R.string.cancel_always, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						FileUtil.deleteFile(bean.getPath());
						finish();
					}
				})
				.setPositiveButton(R.string.upload, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				})
				.create();
		return quitDialog;
	}

	@Override
	public void onBackPressed() {
		if(loadSuccess) {
			super.onBackPressed();
			return;
		}
		createQuitDialog().show();
	}

	//	播放录音
	private void play(FileBean bean){
		if(bean == null)
			return;
		try {
			PlaybackFragment playbackFragment =
					new PlaybackFragment().newInstance(bean);

			FragmentTransaction transaction = (ECSaveFileActivity.this)
					.getFragmentManager()
					.beginTransaction();

			playbackFragment.show(transaction, "dialog_playback");

		} catch (Exception e) {
			MyLog.e("exception", e);
		}
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
		loadSuccess = false;
		MyLog.i("fileBean",bean.getMap().toString());
		ECCallManager.getInstance().upLoadCallFile(bean.getPath(),
				statusHashMap.get("username").toString(),
				"wav",
				bean.getExtraName(),
				bean.getDuration(),
				bean.getFileLength(),new ResponseListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject json) {
				loadSuccess = true;
//			{"fileId":"1465270616703bye.wav","extraName ":"测试","mobile":"18663753236"}
				String fileId = json.getString("fileId");
				bean.setFileName(fileId);
				bean.setFileId(fileId);
				ToastUtil.show(getApplicationContext(),R.string.upload_over);
				finish();
			}

			@Override
			public void onError(RequestError error) {
				loadSuccess = false;
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
