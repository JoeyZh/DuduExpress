package com.joey.expresscall.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joey.expresscall.R;
import com.joey.general.BaseFragment;

public class ECContactsFragment extends BaseFragment {

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.contacts_list_layout,
				container, false);

		return currentView;
	}

	@Override
	public void initSettings() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initUi() {
		// TODO Auto-generated method stub

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
