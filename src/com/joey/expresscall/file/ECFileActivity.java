package com.joey.expresscall.file;

import android.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;

import com.joey.expresscall.R;
import com.joey.general.BaseActivity;

public class ECFileActivity extends BaseActivity{

	private ECFileListFragment fragment;
	private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			finish();
		}
	};
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
		fragment.setItemClickListener(this.itemClickListener);
		
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
