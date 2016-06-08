package com.joey.expresscall.main;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.expresscall.main.bean.CallBean;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.ResourcesUnusualUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ECGroupListActivity extends BaseActivity{
    private ListView listBill;
    private String callListId;
    private final int IDEL_PAGE_NUM = 1;
    private int pageNum;
    private final int PAGE_SIZE = 10;
    private ArrayList<HashMap<String,Object>> mMapList = new ArrayList<HashMap<String, Object>>();

    private ECCallListItemAdapter mAdapter;
    private String[] keys = {"type","toMoible","callTime","extra","color"};
    private int[] ids = {R.id.text_logo,R.id.text_content,R.id.text_desc,R.id.text_extra};
    @Override
    public void initSettings() {
        callListId = getIntent().getStringExtra("callListId");
        getBillList();
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO 账单点击情况
        }
    };
    @Override
    public void initUi() {
        setContentView(R.layout.activity_group_list_layout);
        listBill = (ListView) findViewById(R.id.bill_list);
        listBill.setOnItemClickListener(itemClickListener);
        mAdapter = new ECCallListItemAdapter(this,mMapList,R.layout.simple_item_extra_layout,keys,ids);
        setTitle(R.string.callList);
        listBill.setAdapter(mAdapter);

    }

    @Override
    public void saveSettings() {

    }

    @Override
    public void freeMe() {

    }

    private void getBillList() {
        ECCallManager.getInstance().getCallListDetail(callListId, new ResponseListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject json) {
                JSONArray array = json.getJSONArray("list");
                parseList(array);
            }

            @Override
            public void onError(RequestError error) {

            }

            @Override
            public void onStart() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        createDialog(R.string.waiting, true);
                    }
                });
            }

            @Override
            public void onFinish() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dismissDialog();
                    }
                });
            }
        });
    }

    private void parseList(JSONArray array) {
        if (array.isEmpty()) {
            return;
        }
        if(pageNum == IDEL_PAGE_NUM)
            mMapList.clear();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            CallBean bean = (CallBean) JSON.parseObject(obj.toString(),CallBean.class);
            HashMap<String,Object> map = bean.getMap();
            ResourcesUnusualUtil.register(ECGroupListActivity.this);
            map.put("extra",ResourcesUnusualUtil.getString(CallBean.STATE_CALL_STR + bean.getCallState()));
            map.put("type",ResourcesUnusualUtil.getString("call_logo_"+bean.getCallState()));
            map.put("color",ResourcesUnusualUtil.getString("call_color_"+bean.getCallState()));
            mMapList.add(map);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyLog.i("mapList :"+mMapList.toString());
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
