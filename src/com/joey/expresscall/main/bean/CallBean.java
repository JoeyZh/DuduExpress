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
public class CallBean implements Serializable {


    private String callId;
    private String phone;
    private long callTime;
    private String fileId;

    private int duration;

    public CallBean() {
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }


    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getCallTime() {
        return callTime;
    }

    public void setCallTime(long callTime) {
        this.callTime = callTime;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public HashMap<String, Object> getMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("callId", getCallId());
        String timeStr = DateUtil.convertToDateStr(DateUtil.DATE_FORMMAT_STR_3, getCallTime());
        map.put("callTime", timeStr);
//        map.put("phoneNum",getPhoneNum());
        map.put("callCount", getFileId());
        return map;
    }

    public static CallBean parseJson(String json) {
        CallBean bean = new CallBean();
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            bean.setCallId(jsonObject.getString("callId"));
            bean.setCallTime(jsonObject.getLong("callTime"));
//            bean.setCost(jsonObject.getFloat("money"));
//            bean.setPhoneNum(jsonObject.getString("phoneNum"));
//            bean.setCallCount(jsonObject.getInteger("callCount"));
        } catch (JSONException e) {
            MyLog.e("parse error" + e.getMessage());
        }
        return bean;
    }
}
