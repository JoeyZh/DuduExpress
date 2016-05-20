package com.joey.expresscall.main.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/20.
 */
public class CallListBean implements Serializable{

    private String callListId;
    private ArrayList<CallBean> callList;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    private int totalSize;
    private int successCount;
    private String description;
    private String fileId;

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    private String callTime;

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
    }

    public String getCallListId() {
        return callListId;
    }

    public void setCallListId(String callListId) {
        this.callListId = callListId;
    }

    public ArrayList<CallBean> getCallList() {
        return callList;
    }

    public void setCallList(ArrayList<CallBean> callList) {
        this.callList = callList;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public HashMap<String,Object> getMap(){
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("description",getDescription());
        map.put("callTime",getCallTime());
        map.put("extra",getSuccessCount()+"/"+getTotalSize());
        return map;
    }

    public static CallListBean parseJson(String json){
        CallListBean bean = new CallListBean();
        JSONObject jsonObject = JSON.parseObject(json);
        bean.setCallListId(jsonObject.getString("callListId"));
        bean.setCallTime(jsonObject.getString("callTime"));
        bean.setDescription(jsonObject.getString("description"));
        bean.setSuccessCount(jsonObject.getInteger("successCount"));
        bean.setTotalSize(jsonObject.getInteger("totalSize"));
        bean.setFileId(jsonObject.getString("fileId"));
        return bean;
    }
}
