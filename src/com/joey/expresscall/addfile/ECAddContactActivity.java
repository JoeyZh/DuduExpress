package com.joey.expresscall.addfile;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.joey.expresscall.R;
import com.joey.expresscall.common.CheckableLayout;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.expresscall.contacts.ECContactActivity;
import com.joey.expresscall.contacts.library.ContactsActivity;
import com.joey.general.BaseActivity;
import com.joey.general.utils.CheckPhoneNumber;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Created by Joey on 2016/5/24.
 */
public class ECAddContactActivity extends BaseActivity {

    private ArrayList<HashMap<String, Object>> mSelectList = new ArrayList<HashMap<String, Object>>();
    private ECSimpleAdapter1 mAdapter;
    private CheckableLayout checkableLayout;
    private ListView listView;
    private ImageView imgvSelectContact;
    private EditText editText;
    private HashMap<String, String> matchMap = new HashMap<String, String>();
    private TextWatcher mWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (parseArray()) {
                editText.setText("");
            }
        }
    };
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_contact_layout_checkable:
                    MyLog.i("add_contact_layout_checkable");
                    checkableLayout.toggle();
                    break;
//                case R.id.layout_add_new_contact:
                case R.id.img_add_new_contact:
//                    Intent intent = new Intent(ECAddContactActivity.this, ContactsActivity.class);
//                    startActivity(intent);
//                    MyLog.d("size = "+mAdapter.getSelectList().size());
//                    MyLog.d(mAdapter.getSelectList().toString());
                    parseArray();
                    editText.setText("");
                    break;
            }
        }
    };

    private Runnable refreshListRunnable = new Runnable() {
        @Override
        public void run() {
            mAdapter.notifyDataSetChanged();
            mAdapter.setAllChecked();
            if (checkableLayout.isChecked())
                return;
            checkableLayout.setChecked(true);
        }
    };

    @Override
    public void initSettings() {
        if (statusHashMap.get("contact") != null) {
            mSelectList = (ArrayList<HashMap<String, Object>>) statusHashMap.get("contact");
            for (int i = 0; i < mSelectList.size(); i++) {
                HashMap<String, Object> map = mSelectList.get(i);
                String phone = map.get("number").toString();
                matchMap.put(phone, phone);
            }
            MyLog.i("");
        }
    }

    @Override
    public void initUi() {
        setContentView(R.layout.activity_add_contact);
        checkableLayout = (CheckableLayout) findViewById(R.id.add_contact_layout_checkable);
        checkableLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyLog.i("onCheckChanged");
                if (checkableLayout.isChecked()) {
                    mAdapter.setAllChecked();
                } else {
                    mAdapter.clearChecked();
                }
            }
        });
        checkableLayout.setOnClickListener(clickListener);

        listView = (ListView) findViewById(R.id.list_selected_contacts);
        LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);

        View footer = inflater.inflate(R.layout.common_divider, null);
        listView.addFooterView(footer);
        editText = (EditText) findViewById(R.id.add_contact_edit_text);
        editText.addTextChangedListener(mWatcher);
//        vSelectContact = findViewById(R.id.layout_add_new_contact);
//        vSelectContact.setOnClickListener(clickListener);
        imgvSelectContact = (ImageView) findViewById(R.id.img_add_new_contact);
        imgvSelectContact.setOnClickListener(clickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.e("");
        if (statusHashMap.containsKey("contact")) {
            mSelectList = (ArrayList<HashMap<String, Object>>) statusHashMap.get("contact");
            MyLog.e("onResume", statusHashMap.get("contact").toString());
        }
        mAdapter = new ECSimpleAdapter1(ECAddContactActivity.this, mSelectList, R.layout.simple_item_layout_1, new String[]{"number"}, new int[]{R.id.item_text});
        mAdapter.setEnableChecked(true);
        mAdapter.setType(mAdapter.SIMPLE_ADAPTER_TYPE_TAG);
        listView.setAdapter(mAdapter);
        mAdapter.upDateList(mSelectList);
        if (mSelectList == null || mSelectList.isEmpty()) {
            checkableLayout.setChecked(false);
        } else {
            checkableLayout.setChecked(true);
        }
    }

    @Override
    public void saveSettings() {
        statusHashMap.put("contact", mAdapter.getSelectList());
    }

    @Override
    public void freeMe() {

    }

    private void test() {
        mSelectList.clear();
        matchMap.clear();
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("number", "18512345679" + i);
            matchMap.put("18512345679" + i, "18512345679" + i);
            map.put("checked", false);
            mSelectList.add(map);
        }
    }

    private boolean parseArray() {
        String text = editText.getText().toString().trim();
        if (text.isEmpty())
            return false;
        text = text.replace("，", ",");
        String value[] = text.split(",");
        boolean hasAddContact = false;
        for (int i = 0; i < value.length; i++) {
            value[i] = value[i].trim();
            int matchResult = CheckPhoneNumber.matchNum(value[i]);
            if (matchResult == CheckPhoneNumber.TYPE_ERROR
                    || matchResult == CheckPhoneNumber.TYPE_UNKNOW) {
                continue;
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("number", value[i]);
            map.put("name", value[i]);
            if (matchMap.containsKey(value[i]))
                continue;
            mSelectList.add(map);
            matchMap.put(value[i], value[i]);
            hasAddContact = true;
        }
        runOnUiThread(refreshListRunnable);
        return hasAddContact;
    }

}
