package com.joey.expresscall.protocol;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.joey.general.utils.DateUtil;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.MySharedPreference;
import com.joey.general.utils.ResourcesUnusualUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

/**
 * 请求处理类 ----------------------------------------------- 数据格式说明
 * -----------------------------------------------
 * <dl>
 * { "data": {}, "errorCode": “000”, "msg": "恭喜您，设备绑定成功！", "result": true }
 * </dl>
 *
 * @param <T>
 */
public class RequestHandler<T> {
    private static final String TAG = RequestHandler.class.getSimpleName();
    private final String ERROR_CODE_PREFIX = "web_error_";
    private ResponseListener<T> mResponseListener;
    private String mJson;
    private String mTaskName;

    public RequestHandler(final String result, final ResponseListener<T> l) {
        mResponseListener = l;
        mJson = result;
    }

    public String execute(String taskName) {
        mTaskName = taskName;
        RequestError error = getError();
        if (error == null) {
            T t = convert();
            if (t != null) {
                if (mResponseListener != null) {
                    try {
                        mResponseListener.onSuccess(t);
                    } catch (ClassCastException e) {
                        printError(e);

                        mResponseListener.onError(new RequestError(
                                RequestError.ERROR_CODE_APP_ERROR));
                        // 统计异常
                        String exception = "(-8)类型转换异常(可能是泛型中传递的类型不对)";
                        statisticsException(exception);
                        return exception;
                    } catch (Exception e) {
                        printError(e);

                        mResponseListener.onError(new RequestError(
                                RequestError.ERROR_CODE_APP_ERROR));
                        // 统计异常
                        String exception = "(-9)未知异常(可能是onSuccess中解析Json有问题)";
                        statisticsException(exception);
                        return exception;
                    }
                }
                return "(0)操作成功";
            } else {
                // 统计异常
                String exception = "(-10)获取/解析data信息时出错";
                statisticsException(exception);

                return exception;
            }
        } else {
            intercept(error);
            if (mResponseListener != null) {
                mResponseListener.onError(error);
            }

            String exception = error.errcode + "_" + error.errmsg;
            return exception;
        }

    }

    /**
     * 检测方法返回的json有没有错误<br/>
     *
     * @return
     */
    private RequestError getError() {
        // 空检查
        if (TextUtils.isEmpty(mJson)) {
            return new RequestError(RequestError.ERROR_CODE_SERVER_ERROR);
        }

        // -----------------web接口详细问题记录 Start-------------
        if (mJson.equals("-1")) {
            // 请求超时
            return new RequestError(RequestError.ERROR_CODE_CONNECT_TIMEOUT);
        } else if (mJson.equals("-2")) {
            // 响应超时
            return new RequestError(RequestError.ERROR_CODE_READ_TIMEOUT);
        } else if (mJson.equals("-3")) {
            // 取连接超时
            return new RequestError(RequestError.ERROR_CODE_CONNECTION_POOL_TIMEOUT);
        } else if (mJson.startsWith("-4")) {
            // 其它异常
            return new RequestError(RequestError.ERROR_CODE_WEB_INTERFACE);
        } else if (mJson.startsWith("-5")) {
            // 参数设置时异常
            return new RequestError(RequestError.ERROR_CODE_PARAMS);
        } else if (mJson.equals("-6")) {
            // 参数中有值为null
            return new RequestError(RequestError.ERROR_CODE_VALUE);
        } else if (mJson.startsWith("-7") || mJson.startsWith("-8")) {
            // 读取/解析接口返回信息
            return new RequestError(RequestError.ERROR_CODE_READ_PARSE);
        }
        // -----------------web接口详细问题记录 End----------------

        // 检测json中的result是否为true
        try {
            JSONObject root = JSON.parseObject(mJson);
            int errCode = root.getIntValue("code");
            if (errCode == RequestError.ERROR_CODE_NULL) {
                return null;
            } else {
                String strErrName = ERROR_CODE_PREFIX + errCode;
                String errmsg = ResourcesUnusualUtil.getString(strErrName);
                return new RequestError(errCode, errmsg);
            }
        } catch (Exception e) {
            return new RequestError(
                    RequestError.ERROR_CODE_RESPONSE_FORMAT_ERROR);
        }
    }

    /**
     * <br>
     * 获取Json中的data部分</br> 使用得到的是data开头的Json Object 的格式 有三种
     * ""或者JSONObject，或者JSONArray
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private T convert() {
        Object dataJson = null;
        try {
            dataJson = JSON.parseObject(mJson).get("data");
            MyLog.i("convert = "+dataJson.toString());
            return (T) dataJson;
        } catch (Exception e) {
            mResponseListener.onError(new RequestError(
                    RequestError.ERROR_CODE_RESPONSE_FORMAT_ERROR));
            return null;
        }

    }

    /**
     * 对一些特定的错误进行拦截
     *
     * @param error
     */
    private void intercept(RequestError error) {
        MyLog.e(TAG, "[" + mTaskName + "]" + "error json:" + mJson);
        /** 自定义错误统计(web接口返回的正常错误不再统计) */
        int code = error.errcode;
        if (code >= 1000) {
            // 统计异常
            String exception = error.errcode + "_" + error.errmsg;
            statisticsException(exception);
        }
    }

    // ----------------------------------------------------

    /**
     * 记录异常
     */
    private void printError(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            // 将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }

        MyLog.e(TAG, "[" + mTaskName + "]" + "Throws Exception:" + sw.toString());
    }

    /**
     * 统计异常web接口调用
     */
    private void statisticsException(String exception) {
        String userName = MySharedPreference.getInstance().getString("userName");
        StringBuffer sb = new StringBuffer();
        sb.append(DateUtil.getTime());
        sb.append(" ");
        sb.append("[");
        sb.append(userName);
        sb.append("]");
        sb.append(" ");
        sb.append("[");
        sb.append(mTaskName);
        sb.append("]");
        sb.append("<br/>");
        sb.append(exception);
        Properties prop = new Properties();
        prop.put("exception", sb.toString());
        MyLog.e(TAG, sb.toString());
        MyLog.e(TAG, "[" + mTaskName + "]" + "error json:" + mJson);
//        StatService.trackCustomKVEvent(MainApplication.getInstance(), "web_exception",
//                prop);
    }

}
