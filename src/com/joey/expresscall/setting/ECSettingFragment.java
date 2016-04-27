package com.joey.expresscall.setting;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.joey.expresscall.R;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.general.BaseFragment;

public class ECSettingFragment extends BaseFragment{
	
	private ListView settingListView;
	private View currentView;
	private ECSimpleAdapter1 mAdapter;
	private final int ids[] = {R.id.item_img_logo,R.id.item_text,R.id.item_extra,R.id.item_access};
	private ArrayList<HashMap<String,Object>> mapList;
	private final String keys[]= {"logo","text","extra","access"};
	private final int imgs[] = {R.drawable.icon_album_photo,R.drawable.icon_album_photo,R.drawable.icon_album_photo};
	private String items[];
	public ECSettingFragment(){
	}

	@Override
	protected View createView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.setting_layout, container,false);
		settingListView = (ListView)currentView.findViewById(R.id.setting_list);
		return currentView;
	}

	@Override
	public void initSettings() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initUi() {
		items = getResources().getStringArray(R.array.array_setting_list);
		mapList = new ArrayList<HashMap<String,Object>>();
		for(int i = 0;i<items.length;i++){
			HashMap<String,Object> map = new HashMap<String, Object>();
			map.put(keys[0], imgs[i]);
			map.put(keys[1],items[i]);
			map.put(keys[2],"");
			map.put(keys[3], R.drawable.icon_right_arrow);
			mapList.add(map);
		}
		mAdapter = new ECSimpleAdapter1(currentView.getContext(), mapList, R.layout.simple_item_layout_1, keys, ids);
		settingListView.setAdapter(mAdapter);
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
