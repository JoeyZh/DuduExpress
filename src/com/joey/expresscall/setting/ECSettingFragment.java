package com.joey.expresscall.setting;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseFragment;
import com.joey.general.utils.ToastUtil;
import com.joey.general.views.TopBarLayout;

public class ECSettingFragment extends BaseFragment {

	private View currentView;
	private TopBarLayout mTopBarView;
	private ListView settingListView;
	private ECSimpleAdapter1 mAdapter;
	private final int ids[] = { R.id.item_img_logo, R.id.item_text,
			R.id.item_extra, R.id.item_access };
	private ArrayList<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
	private final String keys[] = { "logo", "text", "extra", "access" };
	private final int imgs[] = { R.drawable.icon_edit,
			R.drawable.icon_time_clock, R.drawable.icon_info };
	private String items[];
	private final int FOOT_ID = -1;
	private ImageView imgLogo;
	private final int TYPE_MODIFY = 1;
	private final int TYPE_TIME_SETTING = 2;
	private final int TYPE_COST_DESC = 3;

	public ECSettingFragment() {
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.setting_layout, container,
				false);
		settingListView = (ListView) currentView
				.findViewById(R.id.setting_list);
		View footer = inflater.inflate(R.layout.layout_logout, null);
		Button logout = (Button) footer.findViewById(R.id.btn_logout);
		footer.setId(FOOT_ID);
		footer.setOnClickListener(clickListener);
		logout.setOnClickListener(clickListener);
		settingListView.addFooterView(footer);

		View header = inflater.inflate(R.layout.layout_setting_head, null);
		header.setOnClickListener(clickListener);
		imgLogo = (ImageView) header.findViewById(R.id.setting_img_user_logo);
		imgLogo.setOnClickListener(clickListener);
		settingListView.addHeaderView(header);
		return currentView;
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case FOOT_ID:
			case R.id.btn_logout:
				logout();
				break;
			case R.id.setting_head_layout:
				Intent intent = new Intent(getActivity(),
						ECModifyNicknameActivity.class);
				startActivity(intent);
				mActivity.overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				break;
			case R.id.left_btn:
				getActivity().onBackPressed();
				break;
			case R.id.setting_img_user_logo:
				break;
			}
		}
	};

	private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			switch (position) {
			case TYPE_MODIFY:
				Intent intent = new Intent(getActivity(),
						ECModifyPwdActivity.class);
				startActivity(intent);
				mActivity.overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				break;
			case TYPE_COST_DESC:
				break;
			case TYPE_TIME_SETTING:
				break;
			}
		}
	};

	@Override
	public void initSettings() {

	}

	@Override
	public void initUi() {
		mTopBarView = getTopBarView();
		mTopBarView.setTopBar(R.drawable.icon_back, -1, R.string.setting,
				clickListener);
		items = getResources().getStringArray(R.array.array_setting_list);
		mapList = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < items.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(keys[0], imgs[i]);
			map.put(keys[1], items[i]);
			map.put(keys[2], "");
			map.put(keys[3], R.drawable.icon_right_arrow);
			mapList.add(map);
		}

		mAdapter = new ECSimpleAdapter1(getActivity(), mapList,
				R.layout.simple_item_layout_1, keys, ids);
		settingListView.setAdapter(mAdapter);

		settingListView.setOnItemClickListener(itemClickListener);
	}

	@Override
	public void saveSettings() {

	}

	@Override
	public void freeMe() {

	}

	private void logout() {
		ECAccountManager.getInstance().logOut(new ResponseListener<String>() {
			@Override
			public void onSuccess(String json) {
				ToastUtil.show(getActivity(), R.string.logout);
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

}
