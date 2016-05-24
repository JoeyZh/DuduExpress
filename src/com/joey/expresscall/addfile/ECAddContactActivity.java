package com.joey.expresscall.addfile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.joey.expresscall.R;
import com.joey.expresscall.common.CheckableLayout;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.expresscall.contacts.ECContactActivity;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Created by Administrator on 2016/5/24.
 */
public class ECAddContactActivity extends BaseActivity{

    private ArrayList<HashMap<String,Object>> mSelectList = new ArrayList<HashMap<String, Object>>();
    private ECSimpleAdapter1 mAdapter;
    private CheckableLayout checkableLayout;
    private ListView listView;
    private View vSelectContact;
    private ImageView imgvSelectContact;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_contact_layout_checkable:
                    MyLog.i("add_contact_layout_checkable");
                    checkableLayout.toggle();
                    break;
                case R.id.layout_add_new_contact:
                case R.id.img_add_new_contact:
                    Intent intent = new Intent(ECAddContactActivity.this,ECContactActivity.class);
                    startActivity(intent);
//                    MyLog.d("size = "+mAdapter.getSelectList().size());
//                    MyLog.d(mAdapter.getSelectList().toString());
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MyLog.d(view.getClass().getName());
           HashMap<String,Object> map = mSelectList.get(position);
            boolean isChecked = (Boolean) map.get("checked");
            map.put("checked",!isChecked);
            mAdapter.notifyDataSetChanged();
        }
    };
    @Override
    public void initSettings() {
        if(statusHashMap.get("contact") != null){
            mSelectList = (ArrayList<HashMap<String,Object>>)statusHashMap.get("contact");
            MyLog.i("");
        }
    }

    @Override
    public void initUi() {
        setContentView(R.layout.activity_add_contact);
        checkableLayout = (CheckableLayout)findViewById(R.id.add_contact_layout_checkable);
        checkableLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyLog.i("onCheckChanged");
                if(checkableLayout.isChecked()){
                    mAdapter.setAllChecked();
                }else {
                    mAdapter.clearChecked();
                }
            }
        });
        checkableLayout.setOnClickListener(clickListener);

        listView = (ListView)findViewById(R.id.list_selected_contacts);
        listView.setOnItemClickListener(itemClickListener);
        LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);

        View footer = inflater.inflate(R.layout.common_divider,null);
        listView.addFooterView(footer);

        vSelectContact = findViewById(R.id.layout_add_new_contact);
        vSelectContact.setOnClickListener(clickListener);
        imgvSelectContact = (ImageView)findViewById(R.id.img_add_new_contact);
        imgvSelectContact.setOnClickListener(clickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.e("");
        if(statusHashMap.containsKey("contact")){
            mSelectList = (ArrayList<HashMap<String,Object>>)statusHashMap.get("contact");
            MyLog.e("onResume",statusHashMap.get("contact").toString());
        }
        mAdapter = new ECSimpleAdapter1(ECAddContactActivity.this,mSelectList,R.layout.simple_item_layout_1,new String []{"number"},new int[]{R.id.item_text});
        mAdapter.setEnableChecked(true);
        mAdapter.setType(mAdapter.SIMPLE_ADAPTER_TYPE_TAG);
        listView.setAdapter(mAdapter);
        mAdapter.upDateList(mSelectList);
        if(mSelectList == null||mSelectList.isEmpty()){
            checkableLayout.setChecked(false);
        }else{
            checkableLayout.setChecked(true);
        }
    }

    @Override
    public void saveSettings() {
        statusHashMap.put("contact",mAdapter.getSelectList());
    }

    @Override
    public void freeMe() {

    }

    private void test(){
        mSelectList.clear();
        for(int i=0;i<10;i++){
            HashMap<String,Object> map = new HashMap<String, Object>();
            map.put("number","18512345679"+i);
            map.put("checked",false);
            mSelectList.add(map);
        }
    }
}
