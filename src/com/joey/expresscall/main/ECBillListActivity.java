package com.joey.expresscall.main;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECCallManager;
import com.joey.expresscall.common.ECSimpleAdapter1;
import com.joey.expresscall.main.bean.BillBean;
import com.joey.expresscall.main.bean.CallBean;
import com.joey.expresscall.main.bean.CallListBean;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseActivity;
import com.joey.general.utils.DateUtil;
import com.joey.general.utils.MyLog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/20.
 */
public class ECBillListActivity extends BaseActivity {

    private ListView listBill;
    private String fileId;
    private final int IDEL_PAGE_NUM = 1;
    private int pageNum;
    private final int PAGE_SIZE = 10;
    private ArrayList<HashMap<String,Object>> mMapList = new ArrayList<HashMap<String, Object>>();

    private ECSimpleAdapter1 mAdapter;
    private String[] keys = {"callTime","toMobile","money","img"};
    private int[] ids = {R.id.item_text_tag,R.id.item_text,R.id.item_extra,R.id.item_access};
    private TextView tvTotal;
    private TextView tvBalance;
    @Override
    public void initSettings() {
       getCallList();
    }

    @Override
    public void initUi() {
        setContentView(R.layout.activity_bill_list_layout);
        listBill = (ListView) findViewById(R.id.bill_list);
        mAdapter = new ECSimpleAdapter1(this,mMapList,R.layout.simple_item_layout_1,keys,ids);
        mAdapter.setType(ECSimpleAdapter1.SIMPLE_ADAPTER_TYPE_TAG);
        tvTotal = (TextView)findViewById(R.id.bill_text_total);
        tvBalance = (TextView)findViewById(R.id.bill_text_balance);
        setTitle(R.string.bill_detail);
        listBill.setAdapter(mAdapter);

    }

    @Override
    public void saveSettings() {

    }

    @Override
    public void freeMe() {

    }

    private void getCallList() {
        String testStart = "2016-06-01 00:00:00 000";
        ECCallManager.getInstance().getBills(DateUtil.getMinTime(DateUtil.DATE_FORMMAT_STR_1,testStart), System.currentTimeMillis(), new ResponseListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject json) {
                JSONArray array = json.getJSONArray("list");
                parseBillList(json);
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
    private void parseBillList(JSONObject object) {
        final float balance = object.getFloat("balance");
        final float total = object.getFloat("total")+balance;
        if(!(object.get("list") instanceof JSONArray)){
            return;
        }

        JSONArray array = object.getJSONArray("list");
        if (array.isEmpty()) {
            return;
        }
        if(pageNum == IDEL_PAGE_NUM)
            mMapList.clear();
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            BillBean bean = (BillBean) JSONObject.parseObject(obj.toString(),BillBean.class);
            mMapList.add(bean.getMap());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvBalance.setText(balance+" 元  ");
                tvTotal.setText(total+" 元  ");
                mAdapter.notifyDataSetChanged();
            }
        });
    }

}
