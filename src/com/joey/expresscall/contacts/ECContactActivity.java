package com.joey.expresscall.contacts;

import android.app.FragmentTransaction;

import com.joey.expresscall.R;
import com.joey.expresscall.contacts.library.SortModel;
import com.joey.expresscall.setting.ECSettingFragment;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MyLog;

import java.util.ArrayList;

public class ECContactActivity extends BaseActivity{
	private ECContactsFragment fragment;

	@Override
	public void initSettings() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initUi() {
		setContentView(R.layout.activity_setting);
		setTopBarVisiable(-1);
		fragment = new ECContactsFragment();
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.setting_content, fragment);
		transaction.commit();
	}

	@Override
	public void saveSettings() {
		if(statusHashMap.containsKey("contact")){
			MyLog.e("statusMap",statusHashMap.get("contact").toString());
		}
	}

	@Override
	public void freeMe() {

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
	}
}
