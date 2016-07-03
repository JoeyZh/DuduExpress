package com.joey.expresscall.file;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.addfile.ECCallingActivity;
import com.joey.expresscall.file.bean.FileBean;

import com.joey.expresscall.main.bean.CallListBean;
import com.joey.expresscall.player.PlaybackFragment;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseFragment;
import com.joey.general.utils.FileUtil;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.MySharedPreference;
import com.joey.general.utils.NetWorkUtil;
import com.joey.general.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ECFileListFragment extends BaseFragment {

	private ListView listView;
	public ECFileItemAdapter mAdapter;
	private final String keys[] = new String[] { "type", "fileName",
			"fileExtra", "createTime", "img","color" };
	private ArrayList<FileBean> fileList = new ArrayList<FileBean>();
	private ArrayList<HashMap<String, Object>> mMapList = new ArrayList<HashMap<String, Object>>();
	private SwipeLayout lastSwipeLayout;
	private AlertDialog dlgDelete;
	private AlertDialog dlgDownload;
	private int operationIndex;
	private OnItemClickListener customListener;
	private View noInfoView;
	private Button btnNoInfo;
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()){
				case R.id.no_info_notice_btn:{
					Intent intent = new Intent(getActivity(), ECCallingActivity.class);
					startActivity(intent);
					getActivity().finish();
				}
					break;
			}
		}
	};
	private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			ToastUtil.show(getActivity(),"点一下");
			if(lastSwipeLayout != null){
				lastSwipeLayout.close(true);
				return;
			}
			FileBean bean = getFileBean(position);
			if(bean == null)
				return;
			mActivity.statusHashMap.put("fileBean",bean);
			if(customListener != null){
				customListener.onItemClick(parent,view,position,id);
				return;
			}
			if(FileUtil.isExist(bean.getPath())){
				play(bean);
				return;
			}

			if(NetWorkUtil.isMobileNetOpen()){
				createDownloadDialog("您正在使用移动网络，下载可能消耗"+mMapList.get(position).get("fileLength")+"的流量");
				dlgDownload.show();
				return;
			}
			download(bean);
		}
	};

	private SimpleSwipeListener swipeListener = new SimpleSwipeListener(){
		@Override
		public void onOpen(SwipeLayout layout) {
			lastSwipeLayout = layout;
		}
		@Override
		public void onClose(SwipeLayout layout) {
			lastSwipeLayout = null;
		}

	};
	public void setItemClickListener(OnItemClickListener listener){
		customListener = listener;
	}
	
	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.fragment_file_list, container,
				false);
		listView = (ListView) currentView.findViewById(R.id.list_files);
		listView.setOnItemClickListener(itemClickListener);
		return currentView;
	}

	@Override
	public void initSettings() {

	}

	@Override
	public void initUi() {
		setTitle(R.string.file_title);
		mAdapter = new ECFileItemAdapter(getActivity(), mMapList,
				 new String[] { "extraName",
				"createTime","fileLength", "img" }, new int[] {
						R.id.text_content, R.id.text_desc, R.id.text_extra,R.id.item_indicator });
		listView.setAdapter(mAdapter);
		mAdapter.setSwipeItemOnClickListener(new ECFileItemAdapter.SwipeItemOnClickListener() {
			@Override
			public void onItemClick(View view, int postion, HashMap<String, Object> map) {

				if(view.getParent().getParent() instanceof SwipeLayout){
					lastSwipeLayout = (SwipeLayout)view.getParent().getParent();
					lastSwipeLayout.close(false);
					lastSwipeLayout = null;
				}
				operationIndex = postion;
				FileBean bean = getFileBean(postion);
				if(bean == null)
					return;
				String msg = "请您再次确定删除文件信息\n" +
						"名称：" +bean.getExtraName()+"\n"+
						"路径："+bean.getPath()+"\n"+
						"创建时间："+mMapList.get(postion).get("createTime")+"\n"+
						"文件大小：" +mMapList.get(postion).get("fileLength").toString();
				createDeleteDialog(msg);
				dlgDelete.show();
			}
		});
		mAdapter.setSimpleSwipeListener(swipeListener);
		loadingInfo();
//		test();
//		暂无文件提示布局
		noInfoView = getView().findViewById(R.id.no_info_notice_layout);
		noInfoView.setVisibility(View.GONE);
		btnNoInfo = (Button)getView().findViewById(R.id.no_info_notice_btn);
		btnNoInfo.setOnClickListener(clickListener);
	}

	private FileBean getFileBean(int position){
		if(operationIndex<0||operationIndex>= fileList.size())
			return null;
		FileBean bean = fileList.get(operationIndex);
		return  bean;
	}

	private void createDeleteDialog(String msg){
		if(dlgDelete != null){
			dlgDelete.setMessage(msg);
			return;
		}
		dlgDelete = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setTitle(R.string.warn_title)
				.setMessage(msg)
				.setPositiveButton(R.string.delete,new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						FileBean bean = getFileBean(operationIndex);
						if(bean == null)
							return;
						deleteFile(bean);
					}
				})
				.setNegativeButton(R.string.cancel,null)
				.create();

	}

	private void createDownloadDialog(String msg){
		if(dlgDownload != null){
			dlgDownload.setMessage(msg);
			return;
		}
		dlgDownload = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
				.setTitle(R.string.warn_title)
				.setMessage(msg)
				.setPositiveButton(R.string.download,new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						FileBean bean = getFileBean(operationIndex);
						if(bean == null)
							return;
						download(bean);
					}
				})
				.setNegativeButton(R.string.cancel,null)
				.create();
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
		if(array == null)
			return;
		if(!isOnTop)
			return;
		mMapList.clear();
		fileList.clear();
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			FileBean bean = FileBean.parseJson(obj.toJSONString());
			fileList.add(bean);
			HashMap<String, Object> map = bean.getMap();
//			map.put("type", bean.getFileType().equals("wav") ? "录" : "文");
//			map.put("color", bean.getFileType().equals("wav") ? "blue" : "red");
			if(FileUtil.isExist(bean.getPath())){
				map.put("img", R.drawable.ic_download_selected);
			}
			else{
				map.put("img", R.drawable.ic_download_normal);
			}
			mMapList.add(bean.getMap());
		}
		fragHandler.post(new Runnable() {
			@Override
			public void run() {
				if(mMapList.isEmpty()){
					noInfoView.setVisibility(View.VISIBLE);
				}
				mAdapter.notifyDataSetChanged();
			}
		});
	}	
//  下载
	private void download(FileBean bean){
		MyLog.e(bean.toString());
		ECCallManager.getInstance().downloadFile(bean.getFileId(), bean.getFileType(),
				bean.getPath(), new ResponseListener<String>() {
					@Override
					public void onSuccess(String json) {
						ToastUtil.show(mActivity,R.string.download_over);
					}

					@Override
					public void onError(RequestError error) {
						ToastUtil.show(mActivity,R.string.download_error);
					}

					@Override
					public void onStart() {
						fragHandler.post(new Runnable() {
							@Override
							public void run() {
								mActivity.createDialog(R.string.downloading,false);
							}
						});
					}

					@Override
					public void onFinish() {
						fragHandler.post(new Runnable() {
							@Override
							public void run() {
								mActivity.dismissDialog();
							}
						});
					}
				});
	}
//	播放
	private void play(FileBean bean){
		try {
			PlaybackFragment playbackFragment =
					new PlaybackFragment().newInstance(bean);

			FragmentTransaction transaction = (getActivity())
					.getFragmentManager()
					.beginTransaction();

			playbackFragment.show(transaction, "dialog_playback");

		} catch (Exception e) {
			MyLog.e("exception", e);
		}
	}

	private void deleteFile(final FileBean bean){
		ECCallManager.getInstance().deleteFile(bean.getFileId(), new ResponseListener<String>() {
			@Override
			public void onSuccess(String json) {
				ToastUtil.show(getActivity(),R.string.delete_success);
				getFiles();
				FileUtil.deleteFile(bean.getPath());
//				parseFiles(json.getJSONArray("list"));
			}

			@Override
			public void onError(RequestError error) {
				ToastUtil.show(getActivity(),R.string.delete_failed);
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

			}
		});
	}
//测试
	private void test() {
		for (int i = 0; i < 10; i++) {
			CallListBean bean = new CallListBean();
			HashMap<String, Object> map = bean.getMap();
			if (i % 2 == 0) {
				map.put(keys[0], "录");
				map.put("color", "blue");
			} else {
				map.put(keys[0], "文");
				map.put("color", "red");
			}
			map.put("img",R.drawable.ic_download_normal);
			// map.put(keys[1], "通知XX小区拿快递");
			// map.put(keys[2], "18666663333,已通知 3/10");
			// map.put(keys[3], "18:00");
			mMapList.add(map);
		}
	}
}
