package com.joey.expresscall.account;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.main.bean.CallListBean;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.expresscall.storage.JVDbHelper;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.MySharedPreference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/6.
 */
public class ECCallListManager {

    private static ECCallListManager listManager;
    private boolean hasRefreshed;
    private HashMap<String, CallListBean> callListMap = new HashMap<String, CallListBean>();
    private ArrayList<HashMap<String, Object>> callMapList = new ArrayList<HashMap<String, Object>>();
    private final int PAGE_SIZE = 10;
    private int callPageNum;
    private final int IDEL_PAGE_NUM = 1;
    private final String accountId;

    public static ECCallListManager getInstance() {
        if (listManager != null)
            return listManager;
        synchronized (ECCallListManager.class) {
            if (listManager == null)
                listManager = new ECCallListManager();
            return listManager;
        }
    }

    private ECCallListManager() {
        accountId = MySharedPreference.getInstance().getString("username");
    }

    public void reset() {
        hasRefreshed = false;
    }

    /**
     * 加载文件
     */
    public void loadLocalData(ResponseListener<ArrayList<HashMap<String, Object>>> listener) {

        Serializable[] list = JVDbHelper.getInstance().getList(JVDbHelper.GROUP_TABLE, "data", JVDbHelper.ACCOUNT_ID, accountId);
        if (list == null || list.length == 0)
            return;
        callMapList.clear();
        callListMap.clear();
        for (int i = 0; i < list.length; i++) {
            CallListBean bean = (CallListBean) list[i];
            callListMap.put(bean.getId(), bean);
            callMapList.add(bean.getMap());
        }
        listener.onSuccess(callMapList);
    }

    /**
     * 加载列表记录
     */
    public void loadCallList(int callPageNum, ResponseListener<ArrayList<HashMap<String, Object>>> listener) {
        if (hasRefreshed)
            return;
        ECCallManager.getInstance().getGroupCallList(callPageNum, PAGE_SIZE,
                new ResponseListener<JSONObject>() {

                    @Override
                    public void onSuccess(JSONObject json) {
                        MyLog.i("callList = " + json.toString());
                        MySharedPreference.getInstance().putString("groupList",
                                json.toJSONString());
                        parseGroupList(json);
                        listener.onSuccess(callMapList);
                    }

                    @Override
                    public void onError(RequestError error) {
                        listener.onError(error);
                    }

                    @Override
                    public void onStart() {
                        listener.onStart();
                    }

                    @Override
                    public void onFinish() {
                        listener.onFinish();
                    }
                });

    }

    private void parseGroupList(JSONObject json) {
        JSONArray array = json.getJSONArray("list");
        if (array == null || array.isEmpty()) {
            return;
        }
        if (callPageNum == IDEL_PAGE_NUM) {
            callListMap.clear();
            callMapList.clear();
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            CallListBean bean = (CallListBean) JSON.parseObject(obj.toString(), CallListBean.class);
//			CallListBean bean = CallListBean.parseJson(obj.toJSONString());
            JVDbHelper.getInstance().insert(bean, accountId, JVDbHelper.GROUP_TABLE);
            callListMap.put(bean.getId(), bean);
            callMapList.add(bean.getMap());
        }
    }


}
