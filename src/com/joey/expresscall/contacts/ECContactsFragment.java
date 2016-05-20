package com.joey.expresscall.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joey.expresscall.R;
import com.joey.expresscall.contacts.library.ContactsActivity;
import com.joey.general.BaseFragment;

import java.util.zip.Inflater;

public class ECContactsFragment extends BaseFragment {

	private View layoutShowContact;
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(),ContactsActivity.class);
			startActivity(intent);
		}
	};

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
		layoutShowContact = currentView.findViewById(R.id.contact_layout_show);
		layoutShowContact.setOnClickListener(clickListener);
		setTitle(R.string.contact);
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
