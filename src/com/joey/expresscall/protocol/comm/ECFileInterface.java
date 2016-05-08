package com.joey.expresscall.protocol.comm;

import java.util.HashMap;

public class ECFileInterface {

	private IHttpComm httpcomm;
	private String token;

	public ECFileInterface() {
		httpcomm = new HttpComm();
	}

	public ECFileInterface(String token) {
		this.token = token;
		httpcomm = new HttpComm();
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String upLoadFile(String path,String fileType,String extraName) {
		String URI = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_LOGIN);
		HashMap param = new HashMap();
		String jsonStr = this.httpcomm
				.httpRequestPost(URI, param);

		return jsonStr;
	}

	public String getFiles() {
		String URI = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_FILE_LIST);
		HashMap param = new HashMap();
		param.put("token", this.token);
		String jsonStr = this.httpcomm
				.httpRequestPost(URI, param);

		return jsonStr;
	}

	public String callArray(String fileId, String fileType, String... phones) {
		String URI = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_CALL);
		HashMap param = new HashMap();
		param.put("token", this.token);
		param.put("fileId", fileId);
		param.put("fileType", fileType);
		param.put("phoneArray", phones);
		String jsonStr = this.httpcomm
				.httpRequestPost(URI, param);

		return jsonStr;
	}

	public String downloadFile(String fileId, String fileType) {
		String URI = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_DOWNLOAD_FILE);
		HashMap param = new HashMap();
		param.put("token", this.token);
		param.put("fileId", fileId);
		param.put("fileType", fileType);
		String jsonStr = this.httpcomm
				.httpRequestPost(URI, param);

		return jsonStr;
	}

	public String getBillDetail(String callId) {
		String URI = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_BILL_DETAIL);
		HashMap param = new HashMap();
		param.put("token", this.token);
		param.put("callId", callId);
		String jsonStr = this.httpcomm
				.httpRequestPost(URI, param);

		return jsonStr;
	}

	public String getBillList() {
		String URI = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_BILL_LIST);
		HashMap param = new HashMap();
		param.put("token", this.token);
		String jsonStr = this.httpcomm
				.httpRequestPost(URI, param);

		return jsonStr;
	}

}
