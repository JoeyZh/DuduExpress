package com.joey.expresscall.main.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.storage.BaseBean;
import com.joey.general.utils.DateUtil;
import com.joey.general.utils.MyLog;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/20.
 */
public class BillBean extends BaseBean{

    private CallBean call;
    private float money;
    private String callId;
    private long callTime;
    private int callCount;
    private String toMobile;

    public String getToMobile() {
        return toMobile;
    }

    public void setToMobile(String phoneNum) {
        this.toMobile = phoneNum;
    }

    public CallBean getCall() {
        return call;
    }

    public void setCall(CallBean call) {
        this.call = call;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float cost) {
        this.money = cost;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        setId(callId);
        this.callId = callId;
    }

    public long getCallTime() {
        return callTime;
    }

    public void setCallTime(long callTime) {
        this.callTime = callTime;
    }

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public HashMap<String,Object> getMap(){
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("callId",getCallId());
        String timeStr = DateUtil.convertToDateStr(DateUtil.DATE_FORMMAT_STR_3, getCallTime());
        map.put("callTime",timeStr);
        map.put("toMobile",getToMobile());
        map.put("money",getMoney()+" 元 ");
        return map;
    }
}
