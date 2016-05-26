package com.joey.expresscall.main;

import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.addfile.ECCallingActivity;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.expresscall.main.bean.BillBean;
import com.joey.expresscall.main.bean.CallBean;
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

    private SimpleAdapter mAdapter;
    private String[] keys = {"callId","callTime","phoneNum"};
    private int[] ids = {R.id.timeline_tag,R.id.timeline_tag_extra,R.id.timeline_content_text};
    @Override
    public void initSettings() {
        callId = getIntent().getStringExtra("callId");
        getBillList();
    }

    @Override
    public void initUi() {
        setContentView(R.layout.activity_bill_list_layout);
        listBill = (ListView) findViewById(R.id.bill_list);
        mAdapter = new SimpleAdapter(this,mMapList,R.layout.timeline_item,keys,ids);
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
            CallBean bean = CallBean.parseJson(obj.toJSONString());
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
