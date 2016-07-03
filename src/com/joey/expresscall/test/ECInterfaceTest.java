package com.joey.expresscall.test;

import java.util.Arrays;
import java.util.Collection;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.AppConsts;
import com.joey.expresscall.protocol.comm.ECAccountInterface;
import com.joey.expresscall.protocol.comm.ECCallInterface;
import com.joey.expresscall.protocol.comm.ECNetUrlConsts;
import com.joey.expresscall.protocol.comm.InterfaceTestDemo;
import com.joey.general.utils.DateUtil;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.ToastUtil;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ECInterfaceTest extends ListActivity {

	// public String test[] = {"账号","通知","文件管理"};
	public String test[] = { "0验证码", "1是否存在", "2注册", "3登陆", "4找回密码", "5修改密码",
			"6用户信息", "7上传", "8获取文件", "9发起呼叫", "10下载", "11获取账单", "12获取详单","13群呼列表",
			"14群呼详情","15注销"};

	public ArrayAdapter adapter;
	public ECAccountInterface account;
	public EditText inputEdit;
	private ECCallInterface fileInterface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		inputEdit = new EditText(this);
		// inputEdit.setHint("")
		RelativeLayout footer = new RelativeLayout(this);
		ListView.LayoutParams params = new ListView.LayoutParams(-1, 100);
		footer.setLayoutParams(params);
		footer.addView(inputEdit, -1, 100);
		getListView().addFooterView(footer);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, test);
		getListView().setAdapter(adapter);
		account = new ECAccountInterface();

	}

	@Override
	protected void onListItemClick(ListView l, View v, final int position,
			long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String response = "";
				String text = inputEdit.getText().toString();
				switch (position) {
				case 0:// 发送验证码
					response = account.validateCode("18663753236");
					JSONObject root = JSONObject.parseObject(response);
					root = root.getJSONObject("Response");
					inputEdit.setText(root.getString("data"));
					break;
				case 1:// 账号存在
					response = account.accountisExist("18663753236");
					break;
				case 2:// 注册
					response = account.register("18663753236", "123456", text);
					break;
				case 3:// 登陆
//					response = account.login("18663753236", "123456");
					response = account.login("18910517619", "654321");
					root = JSONObject.parseObject(response);
					root = root.getJSONObject("data");
					String token = root.getString("token");
					MyLog.i("interfacetest%%%", "token = " + token);

					account.setToken(token);
					fileInterface = new ECCallInterface(token);
					break;
				case 4:// 找回密码
					response = account.forgetPwd("18663753236", "123456", text);
					break;
				case 5:// 修改密码
					response = account.modifyPwd("123456", "999999");
					break;
				case 6:// 获取信息
					response = account.requireInfo();
					break;
				case 7:// 上传
					String name = "哈哈啊哈哈";
					byte [] bytes = name.getBytes();
					try {
						name =  new String(bytes,"UTF-8");
					}catch (Exception e){

					}
					response = "result:"+fileInterface.upLoadFile(AppConsts.RECORD_DIR+"aaa.wav","18663853236","wav",name,10000,10000);
//					try {
//						response = "result:"+InterfaceTestDemo.upload(ECNetUrlConsts.getFullUrl(ECNetUrlConsts.DO_UPLOAD), AppConsts.RECORD_DIR+"byeaaa.wav");
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					break;
				case 8:// 获取文件
					response = fileInterface.getFiles();
					break;
				case 9:// 发起呼叫
					String [] phones = new String[]{"18663753236","15610131752"};
					Arrays.asList(phones);
					response = fileInterface.callArray("bye.wav", "wav","测试文本命令",Arrays.asList(phones));
					break;
				case 10:// 下载
					response = "result:"+fileInterface.downloadFile("bye.wav", "wav",AppConsts.RECORD_DIR+"bye.wav");
					break;
				case 11:// 获取账单
					String testStart = "2016-06-01 00:00:00 000";
					response = fileInterface.getBillList(DateUtil.getMinTime(DateUtil.DATE_FORMMAT_STR_1,testStart),System.currentTimeMillis());
					break;
				case 12:// 获取详单
					response = fileInterface.getBillDetail("1463926736355");
					break;
				case 13:
					response = fileInterface.getCallList(10,1);
					break;
				case 14:
					response = fileInterface.getCallListDetail("1463926011616");
					break;
				case 15:
					response = account.logout();
					break;
				}
				super.run();
				MyLog.i("interfacetest%%%", "response = " + response);
				ToastUtil.show(ECInterfaceTest.this, response);
			}
		}.start();
	}

}
