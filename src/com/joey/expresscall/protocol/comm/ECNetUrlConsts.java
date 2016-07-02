package com.joey.expresscall.protocol.comm;

public class ECNetUrlConsts {
    public final static String DNS = "http://112.74.43.232:8080/SYH/";//"http://jdsc2015.xicp.net/SYH/";
    public final static String DO_LOGIN = "user/login.do";
    public final static String DO_LOGOUT = "user/logout.do";
    public final static String DO_REGIST = "user/register.do";
    public final static String DO_GET_VALIDTECODE = "user/validate.do";
    public final static String DO_FOUND_PWD = "user/forgetPwd.do";
    public final static String DO_INFO = "user/search.do";
    public final static String DO_MODIFY = "user/updatePwd.do";
    public final static String DO_MODIFY_NICKNAME = "user/updateNickName.do";
    public final static String DO_EXIST = "user/search.do";

    // 文件管理相关
    public final static String DO_FILE_LIST = "record/list.do";
    // 发起外呼
    public final static String DO_CALL = "record/call.do";
    // 下载
    public final static String DO_DOWNLOAD_FILE = "record/download.do";
    // 获取账单列表
    public final static String DO_BILL_LIST = "record/billList.do";
    // 获取账单详情
    public final static String DO_BILL_DETAIL = "record/billDetail.do";
    // 上传文件
    public final static String DO_UPLOAD = "record/upload.do";
    // 群呼列表
    public final static String DO_CALL_LIST = "record/groupCallList.do";
    //	群呼列表详情
    public final static String DO_CALL_LIST_DETAIL = "record/billDetail.do";
    //	删除文件
    public final static String DO_DELETE_FILE = "record/deleteFile.do";
    //	重新呼叫
    public final static String DO_REPEAT_CALL = "record/billDetail.do";
    //	删除群呼中的某条呼叫
    public final static String DO_DELETE_PHONE_IN_CALL = "record/deleteFile.do";




    public static String getFullUrl(String url) {
        return DNS + url;
    }
}
