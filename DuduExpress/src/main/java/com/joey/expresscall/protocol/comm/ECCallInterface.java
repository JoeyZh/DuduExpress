package com.joey.expresscall.protocol.comm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.joey.expresscall.AppConsts;

public class ECCallInterface {

	private IHttpComm httpcomm;
	private String token;

	public ECCallInterface() {
		httpcomm = new HttpComm();
	}

	public ECCallInterface(String token) {
		this.token = token;
		httpcomm = new HttpComm();
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * 
	 * @param path
	 * @param phone
	 * @param fileType
	 * @param extraName
	 * @return
	 */
	public boolean upLoadFile(String path,String phone, String fileType, String extraName) {
		String URI = ECNetUrlConsts.getFullUrl(ECNetUrlConsts.DO_UPLOAD);
		HashMap param = new HashMap();
		param.put("mobile", phone);
		param.put("fileType", fileType);
		param.put("extraName", extraName);
		param.put("token", this.token);

		boolean result;
		try {
			result = this.upload(URI, path, param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return result;
	}

	public String getFiles() {
		String URI = ECNetUrlConsts.getFullUrl(ECNetUrlConsts.DO_FILE_LIST);
		HashMap param = new HashMap();
		param.put("token", this.token);
		String jsonStr = this.httpcomm.httpRequestPost(URI, param);

		return jsonStr;
	}

	public String callArray(String fileId, String fileType, List<String> phones) {
		String URI = ECNetUrlConsts.getFullUrl(ECNetUrlConsts.DO_CALL);
		HashMap param = new HashMap();
		param.put("token", this.token);
		param.put("fileId", fileId);
		param.put("fileType", fileType);
		param.put("phoneArray", phones);
		String jsonStr = this.httpcomm.httpRequestPost(URI, param);

		return jsonStr;
	}

	public boolean downloadFile(String fileId, String fileType,String savePath) {
		String URI = ECNetUrlConsts.getFullUrl(ECNetUrlConsts.DO_DOWNLOAD_FILE);
		HashMap param = new HashMap();
		param.put("token", this.token);
		param.put("fileId", fileId);
		param.put("fileType", fileType);
		
		boolean flag = this.httpcomm.httpRequestGetFile(URI, param,savePath);

		return flag;
	}

	public String getBillDetail(String callId) {
		String URI = ECNetUrlConsts.getFullUrl(ECNetUrlConsts.DO_BILL_DETAIL);
		HashMap param = new HashMap();
		param.put("token", this.token);
		param.put("callId", callId);
		String jsonStr = this.httpcomm.httpRequestPost(URI, param);

		return jsonStr;
	}

	public String getBillList() {
		String URI = ECNetUrlConsts.getFullUrl(ECNetUrlConsts.DO_BILL_LIST);
		HashMap param = new HashMap();
		param.put("token", this.token);
		String jsonStr = this.httpcomm.httpRequestPost(URI, param);

		return jsonStr;
	}
	
	private  boolean upload(String urlStr, String filepath,HashMap<String,Object> params) throws Exception {
		String boundary = "------------------------";
		// 分割线
		File file = new File(filepath);

		String fileName = file.getName();
		// 用来解析主机名和端口
		URL url = new URL(urlStr);
		// 用来开启连接
		StringBuilder sb = new StringBuilder();
		Set<String> keys = params.keySet();
		for(String key:keys){
			String value = (String)params.get(key);
			if(key == null||value == null)
				continue;
			sb.append("--" + boundary + "\r\n");
			sb.append("Content-Disposition: form-data; name=\""+key+"\"" + "\r\n");
			sb.append("\r\n");
			sb.append(value+"\r\n");
		}
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

}