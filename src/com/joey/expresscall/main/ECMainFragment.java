package com.joey.expresscall.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.joey.expresscall.R;
import com.joey.general.BaseFragment;

public class ECMainFragment extends BaseFragment{
	
    private View currentView;
    private View addFileView;
    private ListView fileListView;
    private View header;
    private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.add_new_file_layout:
				// TODO Auto-generated method stub

				break;
			}
		}
	};

	
	public ECMainFragment() {
		setTopBarVisiable(-1);
    }

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		  currentView = inflater.inflate(R.layout.main_fragment_layout,
	                container, false);
		  header = inflater.inflate(R.layout.main_user_info, null);
		  fileListView = (ListView)currentView.findViewById(R.id.main_file_list);
		  addFileView = header.findViewById(R.id.add_new_file_layout);
		  addFileView.setOnClickListener(mOnClickListener);
		  fileListView.addHeaderView(header);
		  return currentView;
	}

	
	
}
