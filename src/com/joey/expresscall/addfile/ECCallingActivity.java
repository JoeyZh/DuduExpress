package com.joey.expresscall.addfile;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.expresscall.common.widget.LongPressButton;
import com.joey.expresscall.file.bean.FileBean;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.expresscall.record.AudioRecordFunc;
import com.joey.expresscall.record.ErrorCode;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.ToastUtil;
import com.joey.general.views.TopBarLayout;

public class ECCallingActivity extends BaseActivity {

    private ListView mListView;
    private ECSimpleAdapter1 mAdapter;
    private TabHost tabHost;
    private View vSelectContact;
    private ImageView imgvSelectContact;
    private TextView tvSelectInfo;
    private TextView tvShowList;
    private ArrayList<HashMap<String, Object>> mSelectedNums;

    private ArrayList<HashMap<String, Object>> mData;
    private String tags[];
    private final String keys[] = {"tag", "text", "extra", "access"};
    private final String contents[] = {"电话", ""};
    private final int ids[] = {R.id.item_text_tag, R.id.item_text,
            R.id.item_extra, R.id.item_access};
    private final int access[] = {
            R.drawable.icon_right_arrow, R.drawable.icon_right_arrow};

    private final int LIST_SIZE = 2;
    private FileBean fileBean;
    private EditText editFile;
    private LongPressButton recordBtn;
    private TextView tvRecordTime;
    private LongPressButton.OnLongPressListener longPressListener = new LongPressButton.OnLongPressListener() {
        @Override
        public void onStartPress(View view) {
            MyLog.i("OnLongStart");
            int result = startRecord();
            if(result == ErrorCode.SUCCESS){
                handler.postDelayed(recordTimeRunnable,1000);
                recordBtn.setText(R.string.start_recording);
                return;
            }
            stopRecord();
            recordBtn.setText(R.string.press_to_talk);
        }

        @Override
        public void onFinishPress(View view) {
            MyLog.i("OnLongFinish");
            stopRecord();
        }
    };

    private OnClickListener clickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.layout_select_contact:
                case R.id.img_select_contact:
                    Intent intent = new Intent(ECCallingActivity.this, ECAddContactActivity.class);
                    startActivity(intent);
                    break;
                case R.id.right_btn:
                    startCall();
                    break;
                case R.id.left_btn:
                    onBackPressed();
                    break;
            }
        }
    };

    @Override
    public void initSettings() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initUi() {
        setContentView(R.layout.add_file_layout);
        setTitle(R.string.add_new_file);
        mListView = (ListView) findViewById(R.id.add_file_list);
        mData = new ArrayList<HashMap<String, Object>>();
        tags = getResources().getStringArray(R.array.array_add_tag);
        for (int i = 0; i < LIST_SIZE; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(keys[0], tags[i]);
            map.put(keys[1], contents[i]);
            map.put(keys[3], access[i]);
            mData.add(map);
        }
        mAdapter = new ECSimpleAdapter1(this, mData,
                R.layout.simple_item_layout_1, keys, ids);
        mAdapter.setType(ECSimpleAdapter1.SIMPLE_ADAPTER_TYPE_TAG);
        mListView.setAdapter(mAdapter);

        getTopBarView().setTopBar(R.drawable.icon_back, -1, R.string.add_new_file, clickListener);
        tvSelectInfo = (TextView) findViewById(R.id.text_contacts_selected);
        tvShowList = (TextView) findViewById(R.id.text_contacts_list);
        vSelectContact = findViewById(R.id.layout_select_contact);
        vSelectContact.setOnClickListener(clickListener);
        imgvSelectContact = (ImageView) findViewById(R.id.img_select_contact);
        imgvSelectContact.setOnClickListener(clickListener);
        initTabHost();

    }

    @Override
    public void saveSettings() {
        if (startRecording)
            stopRecord();
    }

    @Override
    public void freeMe() {
        statusHashMap.remove("contact");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (statusHashMap.containsKey("contact")) {
            mSelectedNums = (ArrayList<HashMap<String, Object>>) statusHashMap.get("contact");
            tvSelectInfo.setText(String.format("已选%d人", mSelectedNums.size()));
            if (!mSelectedNums.isEmpty()) {
                getTopBarView().setRightTextRes(R.string.send);
                tvShowList.setText(mSelectedNums.get(0).get("name").toString());
            } else {
                getTopBarView().setRightTextRes(-1);
            }

        }
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
        recordBtn = (LongPressButton) findViewById(R.id.btn_add_file_record);
        recordBtn.setOnLongPressListener(longPressListener);
        tvRecordTime = (TextView) findViewById(R.id.text_record_time);
        tvRecordTime.setText("");
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                MyLog.fmt("tabId %s", tabId);
                if (tabId.equalsIgnoreCase("one")) {
                    editFile.clearFocus();
                    tvRecordTime.setText("");
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

    private void startCall() {
        if (mSelectedNums.isEmpty())
            return;
        ArrayList<String> list = new ArrayList<String>();
        for (HashMap<String, Object> item : mSelectedNums) {
            list.add(item.get("number").toString());
        }
        ECCallManager.getInstance().call(fileBean.getFileId(), fileBean.getFileType(), fileBean.getExtraName(), list, new ResponseListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject json) {

            }

            @Override
            public void onError(RequestError error) {

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private int second;
    private boolean startRecording;
    private Runnable recordTimeRunnable = new Runnable() {
        @Override
        public void run() {
            second++;
            tvRecordTime.setText(second + "\"");
            if (startRecording)
                handler.postDelayed(recordTimeRunnable, 1000);
        }
    };

    //开始录音
    private int startRecord() {
        second = 0;
        startRecording = true;
        tvRecordTime.setText("");
        return AudioRecordFunc.getInstance().startRecordAndFile();
    }

    private void stopRecord() {
        startRecording = false;
        handler.removeCallbacks(recordTimeRunnable);
        AudioRecordFunc.getInstance().stopRecordAndFile();
    }

    private void savedFile() {
        fileBean = new FileBean();
        fileBean.setFileId(AudioRecordFunc.getInstance().getRecordFileName());
        fileBean.setFileType("wav");
        fileBean.setExtraName("测试上传demo");
    }
}
