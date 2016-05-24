package com.joey.expresscall.file;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.file.bean.FileBean;
import com.joey.expresscall.main.ECFileItemAdapter;
import com.joey.expresscall.main.bean.CallListBean;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.expresscall.protocol.TaskBuilder;
import com.joey.expresscall.protocol.comm.ECCallInterface;
import com.joey.general.BaseFragment;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.MySharedPreference;

import java.util.ArrayList;
import java.util.HashMap;

public class ECFileListFragment extends BaseFragment {

	private ListView listView;
	private ECFileItemAdapter mAdapter;
	private final String keys[] = new String[] { "type", "fileName",
			"fileExtra", "createTime", "color" };
	private ArrayList<HashMap<String, Object>> mMapList = new ArrayList<HashMap<String, Object>>();

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.fragment_file_list, container,
				false);
		listView = (ListView) currentView.findViewById(R.id.list_files);
		return currentView;
	}

	@Override
	public void initSettings() {

	}

	@Override
	public void initUi() {
		setTitle(R.string.file_title);
		mAdapter = new ECFileItemAdapter(getActivity(), mMapList,
				R.layout.simple_item_extra_layout, new String[] { "extraName",
						"fileId", "createTime" }, new int[] {
						R.id.text_content, R.id.text_desc, R.id.text_extra });
		listView.setAdapter(mAdapter);
		loadingInfo();

	}

	@Override
	public void saveSettings() {

	}

	@Override
	public void freeMe() {

	}

	private void loadingInfo() {
		String files = MySharedPreference.getInstance().getString("files");
		if (!files.isEmpty())
			parseFiles(JSON.parseArray(files));
		getFiles();
	}

	private void getFiles() {
		ECCallManager.getInstance().getFileList(
				new ResponseListener<JSONObject>() {
					@Override
					public void onSuccess(JSONObject json) {
						MyLog.i("onSuccess", json.toJSONString());
						JSONArray array = json.getJSONArray("list");
						MySharedPreference.getInstance().putString("files",
								array.toJSONString());
						parseFiles(array);

					}

					@Override
					public void onError(RequestError error) {

					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFinish() {

					}
				});
	}

	public void parseFiles(JSONArray array) {
		mMapList.clear();
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			FileBean bean = FileBean.parseJson(obj.toJSONString());
			HashMap<String, Object> map = bean.getMap();
			map.put("type", bean.getFileType().equals("wav") ? "录" : "文");
			map.put("color", bean.getFileType().equals("wav") ? "blue" : "red");
			mMapList.add(bean.getMap());
		}
		fragHandler.post(new Runnable() {
			@Override
			public void run() {
				mAdapter.notifyDataSetChanged();
			}
		});
	}

}
