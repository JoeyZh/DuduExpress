package com.joey.expresscall.main;

import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.expresscall.main.bean.CallBean;
import com.joey.expresscall.main.bean.CallListBean;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ECCallDetailActivity extends BaseActivity{
    private ListView listBill;
    private String callId;
    private final int IDEL_PAGE_NUM = 1;
    private int pageNum;
    private final int PAGE_SIZE = 10;
    private ArrayList<HashMap<String,Object>> mMapList = new ArrayList<HashMap<String, Object>>();

    private ECSimpleAdapter1 mAdapter;
    private String[] keys = {"toMoible","callTime","money"};
    private int[] ids = {R.id.item_text_tag,R.id.item_text,R.id.item_extra};
    @Override
    public void initSettings() {
        callId = getIntent().getStringExtra("callId");
        getBillList();
    }

    @Override
    public void initUi() {
        setContentView(R.layout.activity_bill_list_layout);
        listBill = (ListView) findViewById(R.id.bill_list);
        mAdapter = new ECSimpleAdapter1(this,mMapList,R.layout.simple_item_layout_1,keys,ids);
        setTitle(R.string.bill_detail);
        listBill.setAdapter(mAdapter);

    }

    @Override
    public void saveSettings() {

    }

    @Override
    public void freeMe() {

    }

    private void getBillList() {
        ECCallManager.getInstance().getCallDetail(callId, new ResponseListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject json) {
                MyLog.i(json.toString());
                ToastUtil.show(ECCallDetailActivity.this,json.toJSONString());
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
//            CallBean bean = CallBean.parseJson(obj.toJSONString());
            CallBean bean = (CallBean) JSON.parseObject(obj.toJSONString(),CallBean.class);
            mMapList.add(bean.getMap());
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
