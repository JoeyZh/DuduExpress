package com.joey.expresscall.protocol.comm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class InterfaceTestDemo {

//	public static void main(String[] args) {
//		try {
//			// System.out.println(new Date().getTime());
//			// 注册
//			// Test.doJsonPost("http://127.0.0.1:8080/SYH/user/register.do",
//			// getRegisterStr());
//			// 查询是否存在
//			// Test.doJsonPost("http://127.0.0.1:8080/SYH/user/search.do",
//			// getsearchStr());
//			// 获取验证码
//			// Test.doJsonPost("http://127.0.0.1:8080/SYH/user/validate.do",
//			// "{ \"Request\": { \"dest\": \"18910517619\" }}");
//			// 登录
//			//Test.doJsonPost("http://jdsc2015.xicp.net/SYH/user/login.do", getLoginStr());
//			// 修改密码
//			// Test.doJsonPost("http://127.0.0.1:8080/SYH/user/updatePwd.do",
//			// getupdatePwdStr());
//
//			// 密码找回
//			// Test.doJsonPost("http://127.0.0.1:8080/SYH/user/forgetPwd.do",
//			// getforgetPwdStr());
//			// 修改昵称
//
//			// 查询用户信息
//			// Test.doJsonPost("http://127.0.0.1:8080/SYH/user/search.do",
//			// getsearchStr2());
//
//			// 上传文件 upload(String urlStr, String filepath)
//			// Test.upload("http://jdsc2015.xicp.net/SYH/record/upload.do",
//			// "D:/bye.wav");
//
//			// 获取录音文件列表
//			// Test.doJsonPost("http://jdsc2015.xicp.net/SYH/record/list.do",
//			// getRecordsListStr());
//
//			// 发起外呼
//			// Test.doJsonPost("http://jdsc2015.xicp.net/SYH/record/call.do",
//			// getCallStr());
//
//			// 下载
//			// Test.doJsonPost("http://jdsc2015.xicp.net/SYH/record/download.do",
//			// getdownloadStr());
//
//			// 获取账单列表
//			// Test.doJsonPost("http://jdsc2015.xicp.net/SYH/record/billList.do",
//			 //getbillListStr());
//			 
//			// 获取账单详情
//			// Test.doJsonPost("http://jdsc2015.xicp.net/SYH/record/billDetail.do",	 getbillDetailStr());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

	private static String getbillDetailStr() {
		return "{   \"Request\": {	\"token\": \"189105176191462546792897\",\"callId\": \"1462284306743\"}}";
	}

	private static String getbillListStr() {
		return "{   \"Request\": {	\"token\": \"189105176191462546588884\"}}";
	}

	private static String getdownloadStr() {
		return "{     \"Request\": {    	\"token\": \"189105176191462369537856\",	\"fileId\": \"bye.wav\",\"fileType\": \"wav\"	}}";
	}

	private static String getCallStr() {
		return "{     \"Request\": {    	\"token\": \"189105176191462284126015\",	\"fileId\": \"bye.wav\",\"fileType\": \"wav\",       \"phoneArray\":[	\"18910517616\",\"18910517615\",\"14569874568\"	]	}}";
		// return "{ \"Request\": { \"token\": \"189105176191462284126015\",
		// \"fileId\": \"bye.wav\",\"fileType\": \"wav\", \"phoneArray\":[
		// \"18910517615\" ] }}";
	}

	private static String getRecordsListStr() {

		return "{  \"Request\":{ \"token\":\"189105176191462277182448\" }}";
	}

	private static String getupdatePwdStr() {
		return "{  \"Request\":{ \"token\":\"189105176191461585863771\",	\"oldPwd\":\"123456\",	\"newPwd\": \"654321\" }}";
	}

	private static String getforgetPwdStr() {
		return "{    \"Request\": {    \"userName\": \"18910517619\",	\"password\": \"123456\",	\"validateCode\": \"493961\"  }}";
	}

	private static String getLoginStr() {
		return "{   \"Request\": { \"userName\": \"18910517619\",  \"password\": \"654321\", \"validateCode\": \"823934\"  }}";
	}

	private static String getsearchStr() {
		return "{   \"Request\": {   \"usernName\": \"18910517619\"}}";
	}

	private static String getsearchStr2() {
		return "{   \"Request\": {   \"token\": \"189105176191461585863771\"}}";
	}

	private static String getRegisterStr() {

		return "{ \"Request\": {  \"userName\": \"18910517619\",    \"password\": \"123456\",    \"validateCode\": \"2532\" }}";
	}

	public static boolean upload(String urlStr, String filepath) throws Exception {
		String boundary = "------------------------";
		// 分割线
		File file = new File(filepath);

		String fileName = file.getName();
		// 用来解析主机名和端口
		URL url = new URL(urlStr);
		// 用来开启连接
		StringBuilder sb = new StringBuilder();

		// 添加手机号字段参数
		sb.append("--" + boundary + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"mobile\"" + "\r\n");
		sb.append("\r\n18910517619\r\n");

		// 用来拼装请求其它请求参数
		// token字段
		sb.append("--" + boundary + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"token\"" + "\r\n");
		sb.append("\r\n");
		sb.append("189105176191461590894790" + "\r\n");

		// fileType字段
		sb.append("--" + boundary + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"fileType\"" + "\r\n");
		sb.append("\r\n");
		sb.append("wav" + "\r\n");

		// extraName字段
		sb.append("--" + boundary + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"extraName\"" + "\r\n");
		sb.append("\r\n");
		sb.append("TestTest" + "\r\n");

		// 文件部分
		sb.append("--" + boundary + "\r\n");
		sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + "\r\n");
		sb.append("Content-Type: application/octet-stream" + "\r\n");
		sb.append("\r\n");

		// 将开头和结尾部分转为字节数组，因为设置Content-Type时长度是字节长度
		byte[] before = sb.toString().getBytes("utf-8");
		byte[] after = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");

		// 打开连接, 设置请求头
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(10000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		conn.setRequestProperty("Content-Length", before.length + file.length() + after.length + "");

		conn.setDoOutput(true);
		conn.setDoInput(true);

		// 获取输入输出流
		OutputStream out = conn.getOutputStream();
		FileInputStream fis = new FileInputStream(file);
		// 将开头部分写出
		out.write(before);

		// 写出文件数据
		byte[] buf = new byte[1024 * 5];
		int len;
		while ((len = fis.read(buf)) != -1)
			out.write(buf, 0, len);

		// 将结尾部分写出
		out.write(after);

		InputStream in = conn.getInputStream();
		InputStreamReader isReader = new InputStreamReader(in);
		BufferedReader bufReader = new BufferedReader(isReader);
		String line = null;
		StringBuffer data = new StringBuffer("");
		while ((line = bufReader.readLine()) != null) {
			// data += line;
			data.append(line);
		}

		System.out.println("上传结果：" + data.toString());

		boolean sucess = conn.getResponseCode() == 200;
		in.close();
		fis.close();
		out.close();
		conn.disconnect();

		return sucess;
	}

	public static String doJsonPost(String url, String postStr) throws Exception {

		System.out.println("发送doPost请求 : " + url);
		StringBuffer parameterBuffer = new StringBuffer();

		if (postStr != null) {
			System.out.println("POST parameter : " + postStr);
			parameterBuffer.append(postStr);
		}

		URL localURL = new URL(url);

		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("Content-Type", "application/json");
		httpURLConnection.setRequestProperty("Content-Length", String.valueOf(parameterBuffer.length()));

		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;

		try {
			if (postStr != null) {
				outputStream = httpURLConnection.getOutputStream();
				outputStreamWriter = new OutputStreamWriter(outputStream);

				outputStreamWriter.write(parameterBuffer.toString());
				outputStreamWriter.flush();
			}
			System.out.println("httpURLConnection.getResponseCode()" + httpURLConnection.getResponseCode());
			if (httpURLConnection.getResponseCode() >= 300) {
				throw new Exception(
						"HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
			}

			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}

		} finally {

			if (outputStreamWriter != null) {
				outputStreamWriter.close();
			}

			if (outputStream != null) {
				outputStream.close();
			}

			if (reader != null) {
				reader.close();
			}

			if (inputStreamReader != null) {
				inputStreamReader.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}

		}
		System.out.println("接收到响应字符串：" + resultBuffer.toString());
		return resultBuffer.toString();
	}

}
