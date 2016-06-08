package com.joey.expresscall.main.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.storage.BaseBean;
import com.joey.general.utils.DateUtil;
import com.joey.general.utils.MyLog;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/20.
 */
public class CallBean extends BaseBean {


    private String callId;
    private String toMoible;
    private long callTime;
    private String fileId;
    private long startTime;
    private long endTime;
    private float money;
    private int duration;
    private int callState;

    public static final String STATE_CALL_STR = "call_state_";
    /**
     * 呼叫被拒接
     */
    public static final int STATE_CALL_DENIAL = 1;
    /**
     * 呼叫失败
     */
    public static final int STATE_CALL_FALIED = 2;
    /**
     * 接听成功
     */
    public static final int STATE_CALL_RETURN = 0;


    public CallBean() {
    }

    public int getCallState() {
        return callState;
    }

    public void setCallState(int callState) {
        this.callState = callState;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
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
        setId(callId);
        this.callId = callId;
    }

    public String getToMoible() {
        return toMoible;
    }

    public void setToMoible(String phone) {
        this.toMoible = phone;
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
        map.put("toMoible", getToMoible());
        map.put("money", getMoney());
        map.put("callState", getCallState());
        map.put("startTime", getStartTime());
        map.put("endTime", getEndTime());
        return map;
    }

}
