package com.joey.general.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.joey.general.MainApplication;

import org.apache.http.HttpStatus;
import org.apache.http.conn.util.InetAddressUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Enumeration;

/**
 * 网络相关工具类
 */
public class NetWorkUtil {

    /**
     * Network type is unknown
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;
    private static final String TAG = "NetWorkUtil";
    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    // private static final int NETWORK_TYPE_MOBILE = -100;
    private static final int NETWORK_TYPE_WIFI = -101;

    // 适配低版本手机
    private static final int NETWORK_CLASS_WIFI = -101;
    private static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /**
     * Unknown network class.
     */
    private static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks.
     */
    private static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks.
     */
    private static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks.
     */
    private static final int NETWORK_CLASS_4_G = 3;
    private static Context mContext;
    private static DecimalFormat df = new DecimalFormat("#.##");

    static {
        mContext = MainApplication.getInstance().getContext();
    }

    /**
     * Wifi是否连接
     *
     * @return
     */
    public static boolean isWifiAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo
                .getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * 获取MAC地址
     *
     * @return
     */
    public static String getMacAddress() {
        String localMac = null;
        if (isWifiAvailable()) {
            localMac = getWifiMacAddress();
        }

        if (localMac != null && localMac.length() > 0) {
            localMac = localMac.replace(":", "-").toLowerCase();
            return localMac;
        }

        localMac = getMacFromCallCmd();
        if (localMac != null) {
            localMac = localMac.replace(":", "-").toLowerCase();
        }

        return localMac;
    }

    private static String getWifiMacAddress() {
        String localMac = null;
        try {
            WifiManager wifi = (WifiManager) mContext
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (wifi.isWifiEnabled()) {
                localMac = info.getMacAddress();
                if (localMac != null) {
                    localMac = localMac.replace(":", "-").toLowerCase();
                    return localMac;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 通过callCmd("busybox ifconfig","HWaddr")获取mac地址
     *
     * @return Mac Address
     * @attention 需要设备装有busybox工具
     */
    private static String getMacFromCallCmd() {
        String result = "";
        result = callCmd("busybox ifconfig", "HWaddr");

        if (result == null || result.length() <= 0) {
            return null;
        }

        MyLog.v(TAG, "cmd result : " + result);

        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            String Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            if (Mac.length() > 1) {
                result = Mac.replaceAll(" ", "");
            }
        }

        return result;
    }

    public static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            // 执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 网络是否可用
     *
     * @return
     */
    public static boolean IsNetWorkEnable() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                MyLog.e(TAG, "无法连接网络");
                return false;
            }

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        MyLog.e(TAG, "无法连接网络");
        return false;
    }

    /**
     * 检测网络连接是否真的可用<br/>
     *
     * @return
     */
    public static boolean IsNetWorkConnected() {
        // 使用连接web的方式,不使用ping
        return connectionWebUrl();
    }

    /**
     * 获取运营商
     *
     * @return
     */
    public static String getProvider() {
        String provider = "未知";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) mContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = telephonyManager.getSubscriberId();
            Log.v(TAG, "getProvider.IMSI:" + IMSI);
            if (IMSI == null) {
                if (TelephonyManager.SIM_STATE_READY == telephonyManager
                        .getSimState()) {
                    String operator = telephonyManager.getSimOperator();
                    Log.v(TAG, "getProvider.operator:" + operator);
                    if (operator != null) {
                        if (operator.equals("46000")
                                || operator.equals("46002")
                                || operator.equals("46007")) {
                            provider = "中国移动";
                        } else if (operator.equals("46001")) {
                            provider = "中国联通";
                        } else if (operator.equals("46003")) {
                            provider = "中国电信";
                        }
                    }
                }
            } else {
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
                        || IMSI.startsWith("46007")) {
                    provider = "中国移动";
                } else if (IMSI.startsWith("46001")) {
                    provider = "中国联通";
                } else if (IMSI.startsWith("46003")) {
                    provider = "中国电信";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return provider;
    }

    /**
     * 2G/3G/4G网络判断<br/>
     * return 2G/3G/4G|当前无网络连接
     */
    public static String getSpecialNetWorkInfo() {
        String strNetTips = "";
        String type = getCurrentNetworkType();
        if (TextUtils.equals(type, "2G") || TextUtils.equals(type, "3G")
                || TextUtils.equals(type, "4G")) {
            strNetTips = ResourcesUnusualUtil.getString("net_tip_234g");
        } else if (TextUtils.equals(type, "无")) {
            strNetTips = ResourcesUnusualUtil.getString("net_tip_unavailable");
        }

        return strNetTips;
    }

    /**
     * 获取网络类型
     *
     * @return 各种网络类型
     */
    public static String getCurrentNetworkType() {
        int networkClass = getNetworkClass();
        String type = "未知";
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
                type = "无";
                break;
            case NETWORK_CLASS_WIFI:
                type = "Wi-Fi";
                break;
            case NETWORK_CLASS_2_G:
                type = "2G";
                break;
            case NETWORK_CLASS_3_G:
                type = "3G";
                break;
            case NETWORK_CLASS_4_G:
                type = "4G";
                break;
            case NETWORK_CLASS_UNKNOWN:
                type = "未知";
                break;
        }
        return type;
    }

    private static int getNetworkClass() {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            final NetworkInfo network = ((ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) mContext
                            .getSystemService(
                                    Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNetworkClassByType(networkType);

    }

    private static int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE;
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    public static String getWifiRssi() {
        int asu = 85;
        try {
            final NetworkInfo network = ((ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    WifiManager wifiManager = (WifiManager) mContext
                            .getSystemService(Context.WIFI_SERVICE);

                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        asu = wifiInfo.getRssi();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asu + "dBm";
    }

    public static String getWifiSsid() {
        String ssid = "";
        try {
            final NetworkInfo network = ((ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    WifiManager wifiManager = (WifiManager) mContext
                            .getSystemService(Context.WIFI_SERVICE);

                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null) {
                        ssid = wifiInfo.getSSID();
                        if (ssid == null) {
                            ssid = "";
                        }
                        ssid = ssid.replaceAll("\"", "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ssid;
    }

    /**
     * 检查sim卡状态
     *
     * @param ctx
     * @return
     */
    public static boolean checkSimState() {
        TelephonyManager tm = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT
                || tm.getSimState() == TelephonyManager.SIM_STATE_UNKNOWN) {
            return false;
        }

        return true;
    }

    /**
     * 获取imei
     */
    public static String getImei() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        if (TextUtils.isEmpty(imei)) {
            imei = "000000000000000";
        }
        return imei;
    }

    public static String getPhoneImsi() {
        TelephonyManager mTelephonyMgr = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getSubscriberId();
    }

    /**
     * 唯一的设备ID<br/>
     * 1.先获取手机的ime信息<br/>
     * 2.(1)ime信息不存在,获取wifi的mac地址信息
     */
    public static String getPhoneOnlyCode() {
        String mDeviceOnlyCode = "";
        String ime = getImei();
        if (TextUtils.equals("000000000000000", ime)) {
            String mac = NetWorkUtil.getMacAddress();
            if (!TextUtils.isEmpty(mac)) {
                MyLog.e(TAG, "phone wifi mac:" + mac);
                mDeviceOnlyCode = mac;
            } else {
                MyLog.e(TAG, "phone ime(empty):000000000000000");
                mDeviceOnlyCode = ime;
            }
        } else {
            MyLog.e(TAG, "phone ime:" + ime);
            mDeviceOnlyCode = ime;
        }

        MyLog.e(TAG, "mDeviceOnlyCode:" + mDeviceOnlyCode);

        return mDeviceOnlyCode;
    }

    /**
     * 是否是移动网络环境
     *
     * @return 是否是移动网络
     * @see http
     * ://stackoverflow.com/questions/12806709/how-to-tell-if-mobile-network
     * -data-is-enabled-or-disabled-even-when-connected
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isMobileNetOpen() {
        boolean isMobileNet = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isMobileNet = Settings.Global.getInt(mContext.getContentResolver(), "mobile_data", 1) == 1;
        } else {
            isMobileNet = Settings.Secure.getInt(mContext.getContentResolver(), "mobile_data", 1) == 1;
        }
        return isMobileNet;
    }

    /**
     * 获取本机ip地址
     *
     * @return
     */
    public static String getLocalHostIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                if ("wlan0".equalsIgnoreCase(nif.getName())) {
                    // 遍历每一个接口绑定的所有ip
                    while (inet.hasMoreElements()) {
                        InetAddress ip = inet.nextElement();
                        if (!ip.isLoopbackAddress()
                                && InetAddressUtils.isIPv4Address(ip
                                .getHostAddress())) {
                            return ipaddress = ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipaddress;

    }

    /**
     * 格式化大小
     *
     * @param size
     * @return
     */
    public static String formatSize(long size) {
        String unit = "B";
        float len = size;
        if (len > 900) {
            len /= 1024f;
            unit = "KB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "MB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "GB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "TB";
        }
        return df.format(len) + unit;
    }

    public static String formatSizeBySecond(long size) {
        String unit = "B";
        float len = size;
        if (len > 900) {
            len /= 1024f;
            unit = "KB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "MB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "GB";
        }
        if (len > 900) {
            len /= 1024f;
            unit = "TB";
        }
        return df.format(len) + unit + "/s";
    }

    public static String format(long size) {
        String unit = "B";
        float len = size;
        if (len > 1000) {
            len /= 1024f;
            unit = "KB";
            if (len > 1000) {
                len /= 1024f;
                unit = "MB";
                if (len > 1000) {
                    len /= 1024f;
                    unit = "GB";
                }
            }
        }
        return df.format(len) + "\n" + unit + "/s";
    }

    /**
     * 网络连接判断方式_ping web地址<br/>
     * 此中方式设置超时也不管用
     */
    private static boolean pingWebUrl() {
        // 接口开始执行
        String ip = "www.baidu.com";
        Runtime run = Runtime.getRuntime();
        java.lang.Process proc = null;
        try {
            String str = "ping -c 3 -i 0.2 -W 1 " + ip;
            long starttime = System.currentTimeMillis();
            proc = run.exec(str);
            int retCode = proc.waitFor();
            long difftime = System.currentTimeMillis() - starttime;
            MyLog.v(TAG, "ping耗时:" + difftime);
            if (retCode == 0) {
                MyLog.v(TAG, "ping连接成功");
                return true;
            } else {
                MyLog.e(TAG, "ping测试失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (proc != null) {
                proc.destroy();
            }
        }
        return false;
    }

    /**
     * 网络连接判断方式_使用HttpUrlConnection连接web地址<br/>
     * 此中方式能设置超时
     */
    private static boolean connectionWebUrl() {
        boolean result = false;
        String webUrl = "http://www.baidu.com";
        try {
            URL url = new URL(webUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000); // 设置连接超时为5s
            connection.setReadTimeout(5000); // 读取数据超时也是5s
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpStatus.SC_OK) {
                result = true;
            }
            connection.disconnect();
        } catch (Exception e) {
        }

        if (result) {
            MyLog.v(TAG, "测试连接百度成功");
        } else {
            MyLog.v(TAG, "测试连接百度失败");
        }

        return result;
    }
}
