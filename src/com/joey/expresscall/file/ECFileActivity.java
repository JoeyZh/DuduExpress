package com.joey.expresscall.file;

import android.app.FragmentTransaction;

import com.joey.expresscall.R;
import com.joey.general.BaseActivity;

public class ECFileActivity extends BaseActivity{

	private ECFileListFragment fragment;
	@Override
	public void initSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initUi() {
		setContentView(R.layout.activity_setting);
		setTopBarVisiable(-1);
		fragment = new ECFileListFragment();
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.setting_content, fragment);
		transaction.commit();
		
	}

	@Override
	public void saveSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void freeMe() {
		// TODO Auto-generated method stub
		
	}

}
