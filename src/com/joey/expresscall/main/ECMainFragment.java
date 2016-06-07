package com.joey.expresscall.main;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demievil.library.RefreshLayout;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.addfile.ECCallingActivity;
import com.joey.expresscall.main.bean.CallListBean;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.expresscall.setting.ECSettingActivity;
import com.joey.general.BaseFragment;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.MySharedPreference;

public class ECMainFragment extends BaseFragment {

	private final String keys[] = new String[] { "type", "description",
			"callTime", "extra", "color" };
	private View currentView;
	private View addFileView;
	private ListView fileListView;
	private View header;
	private ECCallListItemAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> mCallList;
	private TextView tvUserName;
	private TextView tvRetain;
	private TextView tvWarning;
	private TextView tvCostDetail;
	private final int IDEL_PAGE_NUM = 1;
	private int callPageNum = IDEL_PAGE_NUM;
	private final int PAGE_SIZE = 10;
	private ImageButton imgBtnSetting;
	private RefreshLayout mRefreshLayout;
	private View footerLayout;
	private TextView textMore;
	private ProgressBar progressBar;
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.add_new_file_layout:
				Intent intent = new Intent(getActivity(),
						ECCallingActivity.class);
				startActivity(intent);
				break;
			case R.id.text_cost_info:
				intent = new Intent(getActivity(), ECBillListActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_user_setting:
				intent = new Intent(getActivity(), ECSettingActivity.class);
				startActivity(intent);
				mActivity.overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);

				break;
			}
		}
	};

	private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(position > mCallList.size())
				return;
			String callListId = mCallList.get(position).get("callListId").toString();
			Intent intent = new Intent(getActivity(),ECGroupListActivity.class);
			intent.putExtra("callListId",callListId);
			startActivity(intent);

		}
	};
	public ECMainFragment() {
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		currentView = inflater.inflate(R.layout.main_fragment_layout,
				container, false);
		setTopBarVisiable(-1);
		header = inflater.inflate(R.layout.main_user_info, null);
		fileListView = (ListView) currentView.findViewById(R.id.main_file_list);
		addFileView = header.findViewById(R.id.add_new_file_layout);
		addFileView.setOnClickListener(mOnClickListener);
		fileListView.addHeaderView(header);
		fileListView.setOnItemClickListener(itemClickListener);
		//初始化footer
		mRefreshLayout = (RefreshLayout) currentView.findViewById(R.id.swipe_container);
		footerLayout = inflater.inflate(R.layout.main_listview_footer, null);
		textMore = (TextView) footerLayout.findViewById(R.id.text_more);
		progressBar = (ProgressBar) footerLayout.findViewById(R.id.load_progress_bar);
//		fileListView.addFooterView(footerLayout);
		return currentView;
	}

	@Override
	public void initSettings() {
		getUseInfo();
		getCallList(IDEL_PAGE_NUM);
	}

	@Override
	public void initUi() {
		mCallList = new ArrayList<HashMap<String, Object>>();
		// test();
		MyLog.fmt("mActicity %s mCallList %s", (mActivity == null) + "",
				(mCallList == null) + "");
		mAdapter = new ECCallListItemAdapter(fileListView.getContext(), mCallList,
				R.layout.simple_item_extra_layout, keys, new int[] {
						R.id.text_logo, R.id.text_content, R.id.text_desc,
						R.id.text_extra,R.id.item_access });

		fileListView.setAdapter(mAdapter);
		// 初始化头部
		tvUserName = (TextView) header.findViewById(R.id.text_user_info);
		tvRetain = (TextView) header.findViewById(R.id.text_cost_retain);
		tvWarning = (TextView) header.findViewById(R.id.text_cost_warning);
		tvCostDetail = (TextView) header.findViewById(R.id.text_cost_info);
		imgBtnSetting = (ImageButton) header
				.findViewById(R.id.btn_user_setting);
		imgBtnSetting.setOnClickListener(mOnClickListener);
		tvCostDetail.setOnClickListener(mOnClickListener);

		//这里可以替换为自定义的footer布局
		//you can custom FooterView
		mRefreshLayout.setChildView(fileListView);
		mRefreshLayout.setColorSchemeResources(R.color.blue_light,
				R.color.main_color,
				R.color.red_light,
				R.color.dot_orange);


		//使用SwipeRefreshLayout的下拉刷新监听
		//use SwipeRefreshLayout OnRefreshListener
		mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				getCallList(IDEL_PAGE_NUM);
			}
		});

		//使用自定义的RefreshLayout加载更多监听
		//use customed RefreshLayout OnLoadListener
		mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
			@Override
			public void onLoad() {
				getCallList(callPageNum++);
			}
		});
	}

	@Override
	public void saveSettings() {
		// TODO Auto-generated method stub

	}

	@Override
	public void freeMe() {
		mRefreshLayout.setRefreshing(false);
	}

	
	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}

	private void loadData() {
		MyLog.i("");
		String userInfo = MySharedPreference.getInstance()
				.getString("userInfo");
		if (!userInfo.isEmpty()) {
			parseUserInfo(JSON.parseObject(userInfo));
		}
		String nickname = MySharedPreference.getInstance().getString("nickname");
		if(!nickname.isEmpty())
			tvUserName.setText(nickname);
		String groupInfo = MySharedPreference.getInstance().getString("groupList");
		if(!groupInfo.isEmpty()){
			parseGroupList(JSON.parseObject(groupInfo));
		}
	}

	private void getUseInfo() {
		// TODO Auto-generated method stubs
		ECAccountManager.getInstance().getUserInfo(
				new ResponseListener<JSONObject>() {

					@Override
					public void onSuccess(JSONObject json) {
						MyLog.i("");
						MySharedPreference.getInstance().putString("userInfo",
								json.toString());
						parseUserInfo(json);
					}

					@Override
					public void onError(RequestError error) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub

					}
				});
	}

	private void parseUserInfo(JSONObject json) {
		final String nickName = json.getString("nickName");
		final float retain = json.getFloat("totalMoney");
		final String mobile = json.getString("mobile");
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (nickName != null)
					tvUserName.setText(nickName);
				else {
					tvUserName.setText(mobile);
				}
				tvRetain.setText(retain + "元");
				if (retain <= 0) {
					tvWarning.setVisibility(View.VISIBLE);
					return;
				}
				tvWarning.setVisibility(View.GONE);
			}

		});
	}

	private void getCallList(int callPageNum) {
		this.callPageNum = callPageNum;
		ECCallManager.getInstance().getGroupCallList(callPageNum, PAGE_SIZE,
				new ResponseListener<JSONObject>() {

					@Override
					public void onSuccess(JSONObject json) {
						MyLog.i("callList = " + json.toString());
						MySharedPreference.getInstance().putString("groupList",
								json.toJSONString());
						parseGroupList(json);
					}

					@Override
					public void onError(RequestError error) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStart() {
						fragHandler.post(new Runnable() {
							@Override
							public void run() {
								mActivity.createDialog(R.string.waiting, true);
							}
						});
					}

					@Override
					public void onFinish() {
						fragHandler.post(new Runnable() {
							@Override
							public void run() {
								mRefreshLayout.setRefreshing(false);
								mActivity.dismissDialog();
							}
						});
					}
				});
	}

	private void parseGroupList(JSONObject json) {
		if(!(json.get("list") instanceof  JSONArray)) {
			return;
		}
		JSONArray array= json.getJSONArray("list");
		if (array == null || array.isEmpty()) {
			return;
		}
		if(!isOnTop)
			return;
		if(callPageNum == IDEL_PAGE_NUM)
			mCallList.clear();
		for (int i = 0; i < array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			CallListBean bean = (CallListBean)JSON.parseObject(obj.toString(),CallListBean.class);
			mCallList.add(bean.getMap());
		}
//		更新UI
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mAdapter.notifyDataSetChanged();
			}
		});
	}

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
			mCallList.add(map);
		}
	}

}
