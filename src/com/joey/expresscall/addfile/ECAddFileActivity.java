package com.joey.expresscall.addfile;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.ListView;

import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.general.BaseActivity;

public class ECAddFileActivity extends BaseActivity{
	
	private ListView mListView;
	private ECSimpleAdapter1 mAdapter;
	private ArrayList <HashMap<String, Object>> mData;
	private String tags[];
	private final String keys[] = {"tag","text","extra","access"};
	private final String contents[] = {"","电话",""};
	private final int ids[] = {R.id.item_text_tag,R.id.item_text,R.id.item_extra,R.id.item_access};
	private final int access[] = {R.drawable.icon_add,R.drawable.icon_right_arrow,R.drawable.icon_right_arrow};

	private final int LIST_SIZE = 3;

	@Override
	public void initSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initUi() {
		setContentView(R.layout.add_file_layout);
		mListView = (ListView)findViewById(R.id.add_file_list);
		mData = new ArrayList<HashMap<String,Object>>();
		tags = getResources().getStringArray(R.array.array_add_tag);
		for(int i=0;i<LIST_SIZE;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(keys[0],tags[i]);
			map.put(keys[1],contents[i]);
			map.put(keys[2],"选择0人");
			map.put(keys[3],access[i]);
			mData.add(map);
		}
		mAdapter = new ECSimpleAdapter1(this, mData, R.layout.simple_item_layout_1, keys, ids);
		mAdapter.setType(ECSimpleAdapter1.SIMPLE_ADAPTER_TYPE_TAG);
		mListView.setAdapter(mAdapter);
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
