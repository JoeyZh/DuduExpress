package com.joey.expresscall.protocol.comm;

import java.util.HashMap;

public class ECAccountInterface {

	private IHttpComm httpcomm;
	private String token;

	public ECAccountInterface() {
		httpcomm = new HttpComm();
	}

	public ECAccountInterface(String token) {
		this.token = token;
		httpcomm = new HttpComm();
	}

	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public String login(String account, String password) {
		String URI_ACCOUNT_LOGIN = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_LOGIN);
		HashMap param = new HashMap();
		param.put("userName", account);
		param.put("password", password);
//		param.put("validateCode", code);
		String jsonStr = this.httpcomm
				.httpRequestPost(URI_ACCOUNT_LOGIN, param);

		return jsonStr;
	}

	public String logout(){
		String URI_ACCOUNT_MODIFYPWD = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_LOGOUT);
		HashMap param = new HashMap();
		param.put("token", this.token);
		String jsonStr = this.httpcomm.httpRequestPost(URI_ACCOUNT_MODIFYPWD,
				param);
		return jsonStr;
	}
	/**
	 * 
	 * @param account
	 * @param password
	 * @param validateCode
	 * @return
	 */
	public String register(String account, String password, String validateCode) {
		String URI_ACCOUNT_REGISTER = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_REGIST);
		HashMap param = new HashMap();
		param.put("userName", account);
		param.put("password", password);
		param.put("validateCode", validateCode);
		String jsonStr = this.httpcomm.httpRequestPost(URI_ACCOUNT_REGISTER,
				param);

		return jsonStr;
	}

	/**
	 * 
	 * @param account
	 * @return
	 */
	public String validateCode(String account) {
		String URI_ACCOUNT_VALIDATECODE = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_GET_VALIDTECODE);
		HashMap param = new HashMap();
		param.put("dest", account);
		String jsonStr = this.httpcomm.httpRequestPost(
				URI_ACCOUNT_VALIDATECODE, param);
		return jsonStr;
	}

	/**
	 * 
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public String modifyPwd(String oldPwd, String newPwd) {
		String URI_ACCOUNT_MODIFYPWD = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_MODIFY);
		HashMap param = new HashMap();
		param.put("token", this.token);
		param.put("oldPwd", oldPwd);
		param.put("newPwd", newPwd);
		// param.put("confirmPwd", confirmPwd);
		// param.put("accinfo", accinfo);
		String jsonStr = this.httpcomm.httpRequestPost(URI_ACCOUNT_MODIFYPWD,
				param);

		return jsonStr;
	}

	/**
	 * 
	 * @param account
	 * @param newPwd
	 * @param validateCode
	 * @return
	 */
	public String forgetPwd(String account, String newPwd, String validateCode) {
		String URI_ACCOUNT_FORGETPWD = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_FOUND_PWD);
		HashMap param = new HashMap();
		param.put("userName", account);
		param.put("password", newPwd);
		// param.put("confirmPwd", confirmPwd);
		param.put("validateCode", validateCode);
		String jsonStr = this.httpcomm.httpRequestPost(URI_ACCOUNT_FORGETPWD,
				param);

		return jsonStr;
	}

	/**
	 * 
	 * @param account
	 * @return
	 */
	public String accountisExist(String account) {
		String URI_ACCOUNT_ACCOUNTEXIST = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_EXIST);
		HashMap param = new HashMap();
		param.put("userName", account);
		String jsonStr = this.httpcomm.httpRequestPost(
				URI_ACCOUNT_ACCOUNTEXIST, param);

		return jsonStr;
	}

	/**
	 * 
	 * @return
	 */
	public String requireInfo() {
		String URI_ACCOUNT_INFO = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_INFO);
		HashMap param = new HashMap();
		param.put("token", this.token);
		String jsonStr = this.httpcomm.httpRequestPost(URI_ACCOUNT_INFO, param);

		return jsonStr;
	}

	/**
	 * 
	 * @param nickName
	 * @return
	 */
	public String modifyInfo(String nickName) {
		String URI_ACCOUNT_MODIFY = ECNetUrlConsts
				.getFullUrl(ECNetUrlConsts.DO_MODIFY);
		HashMap param = new HashMap();
		param.put("token", this.token);
		param.put("nickName", nickName);
		String jsonStr = this.httpcomm.httpRequestPost(URI_ACCOUNT_MODIFY,
				param);

		return jsonStr;
	}
}
