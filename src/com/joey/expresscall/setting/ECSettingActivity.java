package com.joey.expresscall.setting;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseActivity;
import com.joey.general.utils.ToastUtil;
import com.joey.general.views.TopBarLayout;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/23.
 */
public class ECSettingActivity extends BaseActivity {

	private ECSettingFragment fragment;

	@Override
	public void initSettings() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initUi() {
		setContentView(R.layout.activity_setting);
		setTopBarVisiable(-1);
		fragment = new ECSettingFragment();
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
