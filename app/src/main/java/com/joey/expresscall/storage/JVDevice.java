package com.joey.expresscall.storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 设备的序列化抽象
 *
 * @author Joey
 */
public class JVDevice implements Serializable {

    /**
     * default info
     **/
    public static final String DEFAULT_USER = "";
    public static final String DEFAULT_PWD = "";
    /**
     *
     */
    private static final long serialVersionUID = 2;
    /**
     * 设备云视通号
     */
    private String gid;
    /**
     * 设备昵称
     */
    private String nickname;
    /**
     * 设备用户名
     */
    private String username;
    /**
     * 设备接入密码
     */
    private String password;
    /**
     * 设备通道数
     */
    private int channel;
    /**
     * 标记设备是否是分享设备 取值仅限于0或者1, 0表示不是分享设备 1表示是分享设备
     */
    private int permission;
    /**
     * 设备未读报警信息的数量
     */
    private int unReadAlarm;
    /**
     * 设备型号
     */
    private String deviceType;
    /**
     * 设备版本号
     */
    private String versionName;
    /**
     * 设备添加时间
     */
    private String deviceAddTime;

    public JVDevice() {
        username = DEFAULT_USER;
        password = DEFAULT_PWD;
        gid = "";
        channel = 1;
        nickname = gid;
        this.permission = Permission.OWNER;
    }

    public JVDevice(String gid, String username, String password, int channel) {
        this.gid = gid;
        this.nickname = gid.toUpperCase();
        this.username = username;
        this.password = password;
        this.channel = channel;
        this.permission = Permission.OWNER;
    }

    public void initWithJson(JSONObject obj) {
        try {
            this.gid = (obj.getString("gid") + obj.getInt("no")).toUpperCase();
            this.permission = Permission.OWNER;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.username = DEFAULT_USER;
        this.password = DEFAULT_PWD;
        this.nickname = gid.toUpperCase();

    }

    /**
     * {"deviceAddTime":"2015-11-13 11:40:23","deviceGuid":"B223674676",
     * "deviceName":"","deviceUsername":"admin","deviceSdcard":"0",
     * "deviceType":"H411V1_1","deviceChannel":"1",
     * "deviceVersion":"V2.2.4249","permission":"1","devicePassword":"",
     * "unreadAlarm":"0","online":"false"}
     *
     * @param obj 从账号库得到的json转化成对象
     */
    public void initWithJsonOfAccountSDK(com.alibaba.fastjson.JSONObject obj) {
        try {
            this.gid = obj.getString("deviceGuid");
//            this.username = obj.getString("deviceUsername");
//            this.password = obj.getString("devicePassword");
            this.nickname = obj.getString("deviceName");
            this.permission = obj.getInteger("permission");
//            this.unReadAlarm = obj.getIntValue("unreadAlarm");
//            this.deviceType = obj.getString("deviceType");
//            this.versionName = obj.getString("deviceVersion");
            this.deviceAddTime = obj.getString("deviceAddTime");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getGid() {
        return this.gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getChannel() {
        return this.channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getUnReadAlarm() {
        return this.unReadAlarm;
    }

    public void setUnReadAlarm(int count) {
        if (count < 0)
            return;
        this.unReadAlarm = count;
    }

    public int getPermission() {
        return this.permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String type) {
        this.deviceType = type;
    }

    public String getDeviceAddTime() {
        return this.deviceAddTime;
    }

    public void setDeviceAddTime(String time) {
        this.deviceAddTime = time;
    }

    public String getVersion() {
        return this.versionName;
    }

    public void setVersion(String version) {
        this.versionName = version;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return String.format("{\"gid\":\"%s\"," + "\"username\":\"%s\","
                        + "\"password\":\"%s\"," + "\"channel\":%d,"
                        + "\"unReadAlarm\":\"%d\"" + "\"permision\":\"%d\""
                        + "\"deviceType\":\"%s\"," + "\"deviceAddTime\":\"%s\","
                        + "\"version\":\"%s\"," + "}", gid, username, password,
                channel, unReadAlarm, permission, deviceType, deviceAddTime,
                versionName);
    }

    public class Permission {
        public static final int OWNER = 0;
        public static final int SHARE = 1;
    }
}
