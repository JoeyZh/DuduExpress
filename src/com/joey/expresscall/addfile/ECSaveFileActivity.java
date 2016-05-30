package com.joey.expresscall.addfile;

import com.joey.expresscall.R;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.file.bean.FileBean;
import com.joey.general.BaseActivity;

/**
 * Created by Joey on 2016/5/23.
 */
public class ECSaveFileActivity extends BaseActivity {

	private String path;

	@Override
	public void initSettings() {
		
	}

	@Override
	public void initUi() {
		setContentView(R.layout.activity_save_file);
	}

	@Override
	public void saveSettings() {
		// TODO Auto-generated method stub

	}

	@Override
	public void freeMe() {
		// TODO Auto-generated method stub

	}
	
	private void upLoadFile(){
		ECCallManager.getInstance().
	}
}
