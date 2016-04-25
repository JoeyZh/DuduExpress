package com.joey.expresscall.addfile;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MyLog;

public class ECAddFileActivity extends BaseActivity {

	private ListView mListView;
	private ECSimpleAdapter1 mAdapter;
	private TabHost tabHost;

	private ArrayList<HashMap<String, Object>> mData;
	private String tags[];
	private final String keys[] = { "tag", "text", "extra", "access" };
	private final String contents[] = { "", "电话", "" };
	private final int ids[] = { R.id.item_text_tag, R.id.item_text,
			R.id.item_extra, R.id.item_access };
	private final int access[] = { R.drawable.icon_add,
			R.drawable.icon_right_arrow, R.drawable.icon_right_arrow };

	private final int LIST_SIZE = 3;

	private EditText editFile;
	private Button recordBtn;

	@Override
	public void initSettings() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initUi() {
		setContentView(R.layout.add_file_layout);
		mListView = (ListView) findViewById(R.id.add_file_list);
		mData = new ArrayList<HashMap<String, Object>>();
		tags = getResources().getStringArray(R.array.array_add_tag);
		for (int i = 0; i < LIST_SIZE; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(keys[0], tags[i]);
			map.put(keys[1], contents[i]);
			map.put(keys[2], "选择0人");
			map.put(keys[3], access[i]);
			mData.add(map);
		}
		mAdapter = new ECSimpleAdapter1(this, mData,
				R.layout.simple_item_layout_1, keys, ids);
		mAdapter.setType(ECSimpleAdapter1.SIMPLE_ADAPTER_TYPE_TAG);
		mListView.setAdapter(mAdapter);

		initTabHost();

	}

	@Override
	public void saveSettings() {
		// TODO Auto-generated method stub

	}

	@Override
	public void freeMe() {
		// TODO Auto-generated method stub

	}

	private void initTabHost() {
		// 添加布局
		tabHost = (TabHost) findViewById(R.id.tabhost_add_file);
		tabHost.setup();
		// 创建Tab标签
		tabHost.addTab(tabHost.newTabSpec("one").setIndicator("录音")
				.setContent(R.id.add_file_record_layout));
		tabHost.addTab(tabHost.newTabSpec("two").setIndicator("文本")
				.setContent(R.id.add_file_edit_layout));
		editFile = (EditText) findViewById(R.id.edit_add_file_text);
		recordBtn = (Button)findViewById(R.id.btn_add_file_record);
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				MyLog.fmt("tabId %s", tabId);
				if (tabId.equalsIgnoreCase("one")) {
					editFile.clearFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(tabHost.getWindowToken(), 0);
					return;
				}
				if (tabId.equalsIgnoreCase("two")) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					return;
				}
			}
		});
	}

}
