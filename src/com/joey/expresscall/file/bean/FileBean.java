package com.joey.expresscall.file.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.AppConsts;
import com.joey.general.utils.DateUtil;
import com.joey.general.utils.MyLog;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/23.
 */
public class FileBean implements Serializable {

    private String fileId;
    private String path;
    private String fileType;
    private String extraName;
    private int duration;
    private String fileName;
    private long createTime;


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getExtraName() {
        return extraName;
    }

    public void setExtraName(String extraName) {
        this.extraName = extraName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        this.fileType = AppConsts.FILE_DIR+fileName;
    }

    public HashMap<String,Object> getMap(){
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("fileId",getFileId());
        map.put("extraName",getExtraName());
        String timeStr = DateUtil.convertToDateStr(DateUtil.DATE_FORMMAT_STR_3, getCreateTime());
        map.put("createTime",timeStr);
        map.put("path",getPath());
        return map;
    }

    public static FileBean parseJson(String json){
        FileBean bean = new FileBean();
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            bean.setFileId(jsonObject.getString("fileId"));
            bean.setFileType(jsonObject.getString("fileType"));
//            bean.setDuration(jsonObject.getInteger("duration"));
            bean.setCreateTime(jsonObject.getLong("createTime"));
            bean.setExtraName(jsonObject.getString("extraName"));
        }catch (JSONException e){
            MyLog.e("parse error"+e.getMessage());
        }
        bean.setPath(AppConsts.RECORD_DIR+ bean.getFileId());
        return bean;
    }

}
