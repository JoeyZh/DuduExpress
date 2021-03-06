package com.joey.expresscall.main.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.storage.BaseBean;
import com.joey.general.utils.DateUtil;
import com.joey.general.utils.MyLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/20.
 */
public class CallListBean extends BaseBean{

    private String callListId;
    private int totalSize;
    private int successCount;
    private String description;
    private String fileId;
    private long callTime;

    public int getTotalSize() {
        return totalSize;
    }

    public CallListBean() {
        totalSize = 20;
        successCount = 4;
        description = "喇叭喇叭喇叭喇叭";
        callTime = System.currentTimeMillis();
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public long getCallTime() {
        return callTime;
    }

    public void setCallTime(long callTime) {
        this.callTime = callTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
        setId(fileId);
    }

    public String getCallListId() {
        return callListId;
    }

    public void setCallListId(String callListId) {
        this.callListId = callListId;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public HashMap<String,Object> getMap(){
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("fileId",getFileId());
        map.put("callListId",getCallListId());
        map.put("description",getDescription());
        String timeStr = DateUtil.convertToDateStr(DateUtil.DATE_FORMMAT_STR_3, getCallTime());
        map.put("callTime",timeStr);
        map.put("extra",getSuccessCount()+"/"+getTotalSize());
        map.put("type","文");
        map.put("color", "red");
        if(getFileId() == null) {
            return map;
        }
        if(getFileId().toLowerCase().endsWith("wav")){
            map.put("type","录");
            map.put("color", "blue");
            return map;
        }
//        MyLog.i("map = "+map.toString());
        return map;
    }

}
