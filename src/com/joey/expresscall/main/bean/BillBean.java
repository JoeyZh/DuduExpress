package com.joey.expresscall.main.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.joey.general.utils.DateUtil;
import com.joey.general.utils.MyLog;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/20.
 */
public class BillBean implements Serializable{

    private CallBean call;
    private float cost;
    private String callId;
    private long callTime;
    private int callCount;
    private String phoneNum;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public CallBean getCall() {
        return call;
    }

    public void setCall(CallBean call) {
        this.call = call;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
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
//        map.put("phoneNum",getPhoneNum());
        map.put("callCount",getCallCount());
        return map;
    }
    public static BillBean parseJson(String json){
        BillBean bean = new BillBean();
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            bean.setCallId(jsonObject.getString("callId"));
            bean.setCallTime(jsonObject.getLong("callTime"));
            bean.setCost(jsonObject.getFloat("money"));
//            bean.setPhoneNum(jsonObject.getString("phoneNum"));
            bean.setCallCount(jsonObject.getInteger("callCount"));
        }catch (JSONException e){
            MyLog.e("parse error"+e.getMessage());
        }
        return bean;
    }
}
