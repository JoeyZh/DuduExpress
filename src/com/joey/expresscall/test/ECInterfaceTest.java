package com.joey.expresscall.test;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.protocol.comm.ECAccountInterface;
import com.joey.expresscall.protocol.comm.ECFileInterface;
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
			"6用户信息", "7上传", "8获取文件", "9发起呼叫", "10下载", "11获取账单", "12获取详单" };

	public ArrayAdapter adapter;
	public ECAccountInterface account;
	public EditText inputEdit;
	private ECFileInterface fileInterface;

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
					response = account.login("18910517619", "654321");
					root = JSONObject.parseObject(response);
					MyLog.i("interfacetest%%%",
							"login root = " + root.toJSONString());

					root = root.getJSONObject("Response");
					MyLog.i("interfacetest%%%",
							"login response = " + root.toString());

					root = root.getJSONObject("data");
					String token = root.getString("token");
					MyLog.i("interfacetest%%%", "token = " + token);

					account.setToken(token);
					fileInterface = new ECFileInterface(token);
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
					response = fileInterface.upLoadFile("", "wav", "测试一");
					break;
				case 8:// 获取文件
					response = fileInterface.getFiles();
					break;
				case 9:// 发起呼叫
					break;
				case 10:// 下载
					break;
				case 11:// 获取账单
					response = fileInterface.getBillList();
					break;
				case 12:// 获取详单
					response = fileInterface.getBillDetail("");
					break;
				}
				super.run();
				MyLog.i("interfacetest%%%", "response = " + response);
				ToastUtil.show(ECInterfaceTest.this, response);
			}
		}.start();
	}

}
