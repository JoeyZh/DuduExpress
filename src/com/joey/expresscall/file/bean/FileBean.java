package com.joey.expresscall.file.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.AppConsts;
import com.joey.general.utils.DateUtil;
import com.joey.general.utils.MyLog;

import java.io.File;
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
    private long duration;
    private String fileName;
    private long createTime;
    private long fileLength;

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

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
        setPath(AppConsts.RECORD_DIR+fileId);
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        if(getPath() == null){
            this.fileName = fileName;
            return;
        }
        File file = new File(getPath());
        String path = AppConsts.RECORD_DIR+fileName;
        if(file.exists()){
            file.renameTo(new File(path));
        }
        setPath(path);
        this.fileName = fileName;
    }

    public HashMap<String,Object> getMap(){
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("fileId",getFileId());
        map.put("extraName",getExtraName());
        String timeStr = DateUtil.convertToDateStr(DateUtil.DATE_FORMMAT_STR_3, getCreateTime());
        map.put("createTime",timeStr);
        map.put("path",getPath());
        map.put("fileLength",String.format("%.2fKB",getFileLength()/1024.f));
        map.put("duration",getDuration());
        return map;
    }

    public static FileBean parseJson(String json){
        FileBean bean = new FileBean();
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            bean.setFileId(jsonObject.getString("fileId"));
            bean.setFileType(jsonObject.getString("fileType"));
            if(jsonObject.containsKey("duration")&&jsonObject.getLong("duration") != null)
                bean.setDuration(jsonObject.getLong("duration"));
            if(jsonObject.containsKey("fileLength")&&jsonObject.getLong("fileLength") !=null)
                bean.setFileLength(jsonObject.getLong("fileLength"));
            bean.setCreateTime(jsonObject.getLong("createTime"));
            bean.setExtraName(jsonObject.getString("extraName"));
        }catch (JSONException e){
            MyLog.e("parse error"+e.getMessage());
        }
        bean.setPath(AppConsts.RECORD_DIR+ bean.getFileId());
        return bean;
    }

}
