package com.joey.expresscall.main.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/20.
 */
public class CallBean implements Serializable{


    private String callId;
    private String phone;
    private String callTime;

    private int duration;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    private String fileId;

    public CallBean(){}

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

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
